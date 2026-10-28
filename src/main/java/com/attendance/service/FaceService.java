package com.attendance.service;

import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Pipeline;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Service // tells springboot this class is a helper that other classes can use
public class FaceService {

	private ZooModel<Image, float[]> model; // the face model, empty until the first photo comes in

	// loads the face model one time. the first time takes long because it downloads files
	private synchronized ZooModel<Image, float[]> getModel() throws Exception {
		if (model == null) {
			Criteria<Image, float[]> criteria = Criteria.builder()
					.setTypes(Image.class, float[].class) // takes in a picture, gives back a list of numbers
					.optModelUrls("https://resources.djl.ai/test-models/pytorch/face_feature.zip") // where the model is downloaded from
					.optModelName("face_feature")
					.optTranslator(new FaceFeatureTranslator()) // class at the bottom that preps the picture
					.optEngine("PyTorch")
					.build();
			model = criteria.loadModel();
		}
		return model;
	}

	// takes the photo from the webpage and turns it into a list of numbers that describe the face
	public float[] extract(String photoText) throws Exception {
		// the webpage sends "data:image/jpeg;base64,xxxx" so cut off everything before the comma
		String base64 = photoText.substring(photoText.indexOf(',') + 1);
		byte[] bytes = Base64.getDecoder().decode(base64); // turn the text back into picture bytes
		Image photo = ImageFactory.getInstance().fromInputStream(new ByteArrayInputStream(bytes));

		// the webcam photo is wide, so cut out the middle square so the face doesnt get squished
		int size = Math.min(photo.getWidth(), photo.getHeight());
		int x = (photo.getWidth() - size) / 2;
		int y = (photo.getHeight() - size) / 2;
		Image square = photo.getSubImage(x, y, size, size);

		// give the square picture to the model and get the face numbers back
		try (Predictor<Image, float[]> predictor = getModel().newPredictor()) {
			return predictor.predict(square);
		}
	}

	// compares two faces. closer to 1 means the same person, closer to 0 means different people
	public static double cosine(float[] a, float[] b) {
		double dot = 0;
		double lengthA = 0;
		double lengthB = 0;
		for (int i = 0; i < a.length; i++) {
			dot = dot + a[i] * b[i];
			lengthA = lengthA + a[i] * a[i];
			lengthB = lengthB + b[i] * b[i];
		}
		return dot / (Math.sqrt(lengthA) * Math.sqrt(lengthB));
	}

	// turns the list of numbers into text like "0.1,0.2,0.3" so it can be saved in mysql
	public static String toText(float[] numbers) {
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < numbers.length; i++) {
			if (i > 0) {
				text.append(",");
			}
			text.append(numbers[i]);
		}
		return text.toString();
	}

	// turns the saved text back into a list of numbers
	public static float[] fromText(String text) {
		String[] pieces = text.split(",");
		float[] numbers = new float[pieces.length];
		for (int i = 0; i < pieces.length; i++) {
			numbers[i] = Float.parseFloat(pieces[i]);
		}
		return numbers;
	}

	// this small class gets the picture ready for the model (resize and fix the colors)
	private static class FaceFeatureTranslator implements Translator<Image, float[]> {

		@Override
		public NDList processInput(TranslatorContext ctx, Image input) {
			NDArray array = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
			Pipeline pipeline = new Pipeline();
			pipeline.add(new Resize(160, 160)) // model wants 160 by 160 pixels
					.add(new ToTensor())
					.add(new Normalize(
							new float[] {127.5f / 255.0f, 127.5f / 255.0f, 127.5f / 255.0f},
							new float[] {128.0f / 255.0f, 128.0f / 255.0f, 128.0f / 255.0f}));
			return pipeline.transform(new NDList(array));
		}

		@Override
		public float[] processOutput(TranslatorContext ctx, NDList list) {
			return list.singletonOrThrow().toFloatArray(); // the face numbers
		}
	}
}

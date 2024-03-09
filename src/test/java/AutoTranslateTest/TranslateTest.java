package AutoTranslateTest;

import AutoTranslate.Translate;

public class TranslateTest {

	
	public static void main(String[] args) {
		Translate t = new Translate();
		String text = t.getTransSentence("안녕하세요", 0);
		System.out.println(text);
	}
}

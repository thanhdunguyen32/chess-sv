//package test.question;
//
//import game.module.question.bean.QuestionTemplate;
//import game.module.question.dao.QuestionTemplateCache;
//import game.module.question.logic.QuestionManager;
//
//import java.util.List;
//
//public class QuestionTest {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		QuestionTemplateCache.getInstance().reload();
//		QuestionManager qm = QuestionManager.getInstance();
//		List<QuestionTemplate> retList = qm.getRandQuestions(2, 7);
//		System.out.println(retList);
//	}
//
//}

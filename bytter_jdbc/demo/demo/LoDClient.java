package demo;

import java.util.ArrayList;
import java.util.List;

/**
 * LoDԭ��ʾ������ʦ��ѧ��������Ŀα�
 * @author pangl
 *
 */
public class LoDClient {
	
	public static void main(String[] args) {
		List<Book> books = new ArrayList<Book>();
		for (int i=0; i<10; i++) {
			boolean isChinese = Math.random() > 0.5;
			books.add(new Book(isChinese));
		}
		Teacher teacher = new Teacher();
		Student student = new Student();
		teacher.command(student, books);
	}
}

/**
 * ��ʦ��
 * @author pangl
 */
class Teacher {
	/**
	 * ����ѧ��������Ŀα�
	 * @param stud
	 * @param books
	 */
	public void command(Student stud, List<Book> books) {
		stud.countChinese(books);
	}
	
}

/**
 * �α���
 * @author pangl
 */
class Book {
	private boolean isChinese;//�Ƿ����Ŀα�
	public Book(boolean isChinese) {
		this.isChinese = isChinese;
	}
	public boolean isChinese() {
		return isChinese;
	}
}

/**
 * ѧ����
 * @author pangl
 */
class Student {
	/**
	 * ������Ŀα�
	 * @param books
	 */
	public void countChinese(List<Book> books) {
		int count = 0;
		for (Book book : books) {
			if (book.isChinese())
				count ++;
		}
		System.out.println("�ܹ���" + count + "�����Ŀα�");
	}
}
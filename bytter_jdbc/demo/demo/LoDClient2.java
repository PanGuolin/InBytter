package demo;

import java.util.ArrayList;
import java.util.List;

/**
 * LoDԭ��ʾ������ʦ��ѧ��������Ŀα�
 * @author pangl
 *
 */
public class LoDClient2 {
	
	public static void main(String[] args) {
		List<Book2> books = new ArrayList<Book2>();
		for (int i=0; i<10; i++) {
			boolean isChinese = Math.random() > 0.5;
			books.add(new Book2(isChinese));
		}
		Teacher2 teacher = new Teacher2();
		Student2 student = new Student2();
		student.setBooks(books);
		teacher.command(student);
	}
}

/**
 * ��ʦ��
 * @author pangl
 */
class Teacher2 {
	/**
	 * ����ѧ��������Ŀα�
	 * @param stud
	 * @param books
	 */
	public void command(Student2 stud) {
		stud.countChinese();
	}
	
}

/**
 * �α���
 * @author pangl
 */
class Book2 {
	private boolean isChinese;//�Ƿ����Ŀα�
	public Book2(boolean isChinese) {
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
class Student2 {
	private List<Book2> books;
	
	public void setBooks(List<Book2> books) {
		this.books = books;
	}
	
	/**
	 * ������Ŀα�
	 * @param books
	 */
	public void countChinese() {
		int count = 0;
		for (Book2 book : books) {
			if (book.isChinese())
				count ++;
		}
		System.out.println("�ܹ���" + count + "�����Ŀα�");
	}
}
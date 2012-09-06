package demo;

import java.util.ArrayList;
import java.util.List;

/**
 * LoD原则示例：老师让学生清点语文课本
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
 * 老师类
 * @author pangl
 */
class Teacher2 {
	/**
	 * 命令学生请点语文课本
	 * @param stud
	 * @param books
	 */
	public void command(Student2 stud) {
		stud.countChinese();
	}
	
}

/**
 * 课本类
 * @author pangl
 */
class Book2 {
	private boolean isChinese;//是否语文课本
	public Book2(boolean isChinese) {
		this.isChinese = isChinese;
	}
	public boolean isChinese() {
		return isChinese;
	}
}

/**
 * 学生类
 * @author pangl
 */
class Student2 {
	private List<Book2> books;
	
	public void setBooks(List<Book2> books) {
		this.books = books;
	}
	
	/**
	 * 清点语文课本
	 * @param books
	 */
	public void countChinese() {
		int count = 0;
		for (Book2 book : books) {
			if (book.isChinese())
				count ++;
		}
		System.out.println("总共有" + count + "本语文课本");
	}
}
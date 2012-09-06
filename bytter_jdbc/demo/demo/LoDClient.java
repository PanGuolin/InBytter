package demo;

import java.util.ArrayList;
import java.util.List;

/**
 * LoD原则示例：老师让学生清点语文课本
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
 * 老师类
 * @author pangl
 */
class Teacher {
	/**
	 * 命令学生清点语文课本
	 * @param stud
	 * @param books
	 */
	public void command(Student stud, List<Book> books) {
		stud.countChinese(books);
	}
	
}

/**
 * 课本类
 * @author pangl
 */
class Book {
	private boolean isChinese;//是否语文课本
	public Book(boolean isChinese) {
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
class Student {
	/**
	 * 请点语文课本
	 * @param books
	 */
	public void countChinese(List<Book> books) {
		int count = 0;
		for (Book book : books) {
			if (book.isChinese())
				count ++;
		}
		System.out.println("总共有" + count + "本语文课本");
	}
}
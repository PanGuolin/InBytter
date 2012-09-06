package demo;

/**
 * 长方形
 * @author pangl
 */
class Rectangle {
	private int width;//宽
	private int length;//高
	
	public int getWidth() {
		return width;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public void setWidth(int width) {
		this.width = width;
	}
}

/** 
 * 正方形
 * @author pangl
 */
class Square extends Rectangle {

	@Override
	public void setLength(int length) {
		super.setLength(length);
		super.setWidth(length);
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		super.setLength(width);
	}
}

/**
 * 客户端
 * @author pangl
 */
public class RectangleClient {
	public static void main(String[] args) {
		Rectangle rect = new Rectangle();
		rect.setWidth(100);
		while(rect.getLength() <= rect.getWidth()) {
			rect.setLength(rect.getWidth() + 10);
		}
		System.out.println("Length:" + rect.getLength() 
				+ ", Width:" + rect.getWidth());
	}
}

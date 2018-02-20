package learn_itext;

public class BarcodeInfo {

	private String	icn;
	private int		itemNum;
	private int		pieceCount;
	private int		totalItems;

	public BarcodeInfo(final String icn, final int totalItems, final int pieceCount) {
		this.icn = icn;
		this.totalItems = totalItems;
		this.pieceCount = pieceCount;
		this.itemNum = Integer.parseInt(icn.split("-")[4]);
	}

	public String getIcn() {
		return this.icn;
	}

	public int getItemNum() {
		return this.itemNum;
	}

	public int getPieceCount() {
		return this.pieceCount;
	}

	public int getTotalItems() {
		return this.totalItems;
	}

	public void setIcn(final String icn) {
		this.icn = icn;
	}

	public void setItemNum(final int itemNum) {
		this.itemNum = itemNum;
	}

	public void setPieceCount(final int pieceCount) {
		this.pieceCount = pieceCount;
	}

	public void setTotalItems(final int totalItems) {
		this.totalItems = totalItems;
	}
}
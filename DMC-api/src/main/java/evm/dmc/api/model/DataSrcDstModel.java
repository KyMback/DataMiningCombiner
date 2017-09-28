package evm.dmc.api.model;

public class DataSrcDstModel extends FunctionModel {
	private String srcdst = null;
	private DataSrcDstType typeSrcDst = DataSrcDstType.LOCAL_FS;
	
	public DataSrcDstModel(){
		super();
	}
	
	public DataSrcDstModel(FunctionModel funmodel){
		super(funmodel);
	}

	/**
	 * @return the sourceDest
	 */
	public String getSourceDest() {
		return srcdst;
	}

	/**
	 * @param sourceDest the sourceDest to set
	 */
	public void setSourceDest(String sourceDest) {
		this.srcdst = sourceDest;
	}

	/**
	 * @return the type
	 */
	public DataSrcDstType getTypeSrcDst() {
		return typeSrcDst;
	}

	/**
	 * @param type the type to set
	 */
	public void setTypeSrcDst(DataSrcDstType type) {
		this.typeSrcDst = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((srcdst == null) ? 0 : srcdst.hashCode());
		result = prime * result + ((typeSrcDst == null) ? 0 : typeSrcDst.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataSrcDstModel other = (DataSrcDstModel) obj;
		if (srcdst == null) {
			if (other.srcdst != null)
				return false;
		} else if (!srcdst.equals(other.srcdst))
			return false;
		if (typeSrcDst != other.typeSrcDst)
			return false;
		return true;
	}
	

}
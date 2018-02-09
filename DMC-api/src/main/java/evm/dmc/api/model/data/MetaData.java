package evm.dmc.api.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.converters.StringListConverter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name="METADATA")
public class MetaData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) 
	private Long id;
	
	private String name;
	
	private String description;
	
	/**
	 * what project this data belongs to
	 */
	@ManyToOne(cascade = CascadeType.ALL)
//	@NotNull
	@JoinColumn(name = "project_id")
	private ProjectModel project;

	@ElementCollection
	@MapKeyColumn(name = "ATTR_NAME")
	@Column(name = "ATTRIBUTE")
	@CollectionTable(name = "DATA_ATTRIBUTES", joinColumns=@JoinColumn(name="METADATA_ID"))
//	@Setter
//	@Getter
	private Map<String, DataAttribute> attributes = new HashMap<>();
	
	@Column
//	@Column(columnDefinition = "TEXT")
	@Lob
//	@Convert(converter = StringListConverter.class)
	@ElementCollection
	@CollectionTable(name = "PREVIEW_DATA", joinColumns=@JoinColumn(name="METADATA_ID"))
	private List<String> preview = new ArrayList<>();
	
	// delimiter for preview
	// also can be used for parsing data files instead of storage.delimiter
	private String delimiter = ",;\t|";
	
	private boolean hasHeader = true;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "storage_id")
	private DataStorageModel storage;
	
	public void setStorage(DataStorageModel storage) {
		storage.setMeta(this);
		this.storage = storage;
	}
	
	/**
	 * @return unmodifiable List
	 */
	public List<String> getPreview() {
		return Collections.unmodifiableList(preview);
	}
	
	/**
	 * Creates new content of Attributes map, using argument list
	 * @param list
	 */
	public void setAttributesAsList(List<DataAttribute> list) {
		list.stream().forEach(attr -> attributes.put(attr.getName(), attr));
	}
	
	/**
	 * Copies preview values to existing attributes
	 * Names of attributes must match
	 * @param list
	 */
	public void setAttributesPreview(List<DataAttribute> list) {
		list.stream().forEach(attr -> attributes.get(attr.getName()).setLines(attr.getLines()));
	}
}

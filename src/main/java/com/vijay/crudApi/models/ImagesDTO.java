package com.vijay.crudApi.models;

public class ImagesDTO {

	private Long id;
	private String imageData;

	public ImagesDTO(Long id, String imageData) {
		this.id = id;
		this.imageData = imageData;  // Api will get this name for Object
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}
}
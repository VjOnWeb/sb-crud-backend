package com.vijay.crudApi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vijay.crudApi.models.Note;
import com.vijay.crudApi.models.userRest;
class CrudApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testCreateAndGetUser() throws Exception {
		userRest user = new userRest();
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setAge(30);
		user.setOccupation("Developer");

		mockMvc.perform(post("/api/save")  // mapped in your controller
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(content().string("User Created"));

		mockMvc.perform(get("/api/users"))
				.andExpect(status().isOk());
	}

	@Test
	void testUploadImage() throws Exception {
		MockMultipartFile file = new MockMultipartFile(
				"file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "dummy image content".getBytes());

		mockMvc.perform(multipart("/api/images/upload").file(file))
				.andExpect(status().isOk());
	}

	@Test
	void testCreateNoteAndList() throws Exception {
		Note note = new Note();
		note.setTitle("Test Note");
		note.setContent("This is a sample note.");

		mockMvc.perform(post("/api/notes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(note)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists());

		mockMvc.perform(get("/api/notes"))
				.andExpect(status().isOk());
	}
}

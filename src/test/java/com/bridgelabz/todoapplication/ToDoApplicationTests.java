package com.bridgelabz.todoapplication;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToDoApplicationTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webappcontext;

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webappcontext).build();
	}

	@Test
	public void loginTestPass() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
				.content("{ \"email\" : \"yugandharap2013@gmail.com\",\"password\" : \"yuga@123\"}")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("you are successfully logged in"))
				.andExpect(jsonPath("$.status").value(1));
	}
	@Test
	public void loginTestEmailCheck() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
				.content("{ \"email\" : \"yugandharap2011@gmail.com\",\"password\" : \"yuga@123\"}")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("NullPointerException: Email id not present in Database"))
				.andExpect(jsonPath("$.status").value(-1));
	}
	
	@Test
	public void loginTestPasswordCheck() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
				.content("{ \"email\" : \"yugandharap2013@gmail.com\",\"password\" : \"yuga@1234\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").value("Wrong Password given"))
				.andExpect(jsonPath("$.status").value(-1));
	}

	
	
	@Test
	public void loginTestNullEmailCheck() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
				.content("{ \"password\" : \"yuga@123\"}")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("NoValuePresent : Email should not be null"))
				.andExpect(jsonPath("$.status").value(-1));
	}
	
	@Test
	public void loginTestNullPasswordCheck() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
				.content("{ \"email\" : \"yugandharap2013@gmail.com\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").value("NoValuePresent : Password should not be null"))
				.andExpect(jsonPath("$.status").value(-1));
	}
}

package com.rich.AsistenteEscolar.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
public class ChatRequest {

	public String model;
	public List<Message> messages;
	public int n;
	public double temperature;
	
	public ChatRequest(String model, int n,double temperature) {
		this.model = model;
		this.n = n;
		this.temperature = temperature;
		
	}

}

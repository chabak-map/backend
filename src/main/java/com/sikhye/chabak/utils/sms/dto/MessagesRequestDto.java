package com.sikhye.chabak.utils.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Data
@Service
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessagesRequestDto {
	private String to;
	private String content;
}

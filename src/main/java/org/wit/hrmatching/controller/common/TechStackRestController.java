package org.wit.hrmatching.controller.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wit.hrmatching.service.employer.TechStackService;
import org.wit.hrmatching.vo.TechStackVO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tech-stacks")
public class TechStackRestController {

	private final TechStackService techStackService;

	@GetMapping
	public ResponseEntity<List<TechStackVO>> getAllTechStacks() {
		List<TechStackVO> techStacks = techStackService.getAllStacks();
		return ResponseEntity.ok(techStacks);
	}
}

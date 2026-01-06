package com.study.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.springboot.dao.IregisterDAO;
import com.study.springboot.dto.registerDTO;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class registerController {
	@Autowired
	IregisterDAO dao;
	
	@RequestMapping("/") // localhost:8080/
	public String root() {
		return "redirect:list"; //WEB-INF/views/list.jsp
	}
	
	@RequestMapping("/writeForm") // localhost:8080/writeForm
	public String writeForm() {
		return "writeForm"; //WEB-INF/views/writeForm.jsp
	}
	
	@RequestMapping("/write") // localhost:8080/write
	public String write(HttpServletRequest request,registerDTO dto) {
		// 연락처 합쳐서 저장
		String rtel1 = request.getParameter("rtel1");
		String rtel2 = request.getParameter("rtel2");
		String rtel3 = request.getParameter("rtel3");
		dto.setRtel(rtel1+"-"+rtel2+"-"+rtel3);
		
		// 취미를 배열처리해서 저장
		String[] hobbies = request.getParameterValues("rhobby");
		String chk ="";
		
		// 체크박스를 체크 했다면,
		if(hobbies != null) {
			for(int i=0;i<hobbies.length;i++) {
				chk = chk+hobbies[i]+" ";
			}
			dto.setRhobby(chk);
		}else {
			dto.setRhobby(" ");
		}
		
		dao.writeDao(dto); // insert 실행
		return "redirect:list"; //WEB-INF/views/list.jsp
	}
	
	@RequestMapping("/list")
	public String list(Model model) {
		 model.addAttribute("list", dao.listDao());
		 
		 return "list";
	}
	
	@RequestMapping("/detail")
	public String detail(HttpServletRequest request, Model model) {
		int rno = Integer.parseInt(request.getParameter("rno"));
		model.addAttribute("detail", dao.viewDao(rno));
		return "detail";
	}
	
	// 비밀번호 확인 폼 (수정/탈퇴 공용)
	@RequestMapping("/passwordCheckForm")
	public String passwordCheckForm(HttpServletRequest request, Model model) {
		int rno = Integer.parseInt(request.getParameter("rno"));
		String mode = request.getParameter("mode"); //update, delete
		
		model.addAttribute("rno", rno);
		model.addAttribute("mode", mode);
		
		return "passwordCheckForm";
	}
	
	// 비밀번호 확인 처리
	@RequestMapping("/passwordCheck")
	public String passwordCheck(HttpServletRequest request, Model model) {
		int rno = Integer.parseInt(request.getParameter("rno"));
		String mode = request.getParameter("mode");
		String rpasswd = request.getParameter("rpasswd");
		
		if(dao.checkPasswordDao(rno, rpasswd)) {
			if(mode.equals("update")) {
				
				model.addAttribute("edit", dao.viewDao(rno));
				return "updateForm";
			}
			else if(mode.equals("delete")) {
				dao.deleteDao(rno);
				return "redirect:list";
			}
		}
		model.addAttribute("msg", "비밀번호가 틀렸습니다.");
		model.addAttribute("rno", rno);
		model.addAttribute("mode", mode);
		return "passwordCheckForm";
	}
	
	
	@RequestMapping("/update")
	public String update(HttpServletRequest request,registerDTO dto) {
		// 연락처 합쳐서 저장
		String rtel1 = request.getParameter("rtel1");
		String rtel2 = request.getParameter("rtel2");
		String rtel3 = request.getParameter("rtel3");
		dto.setRtel(rtel1+"-"+rtel2+"-"+rtel3);
		
		// 취미를 배열처리해서 저장
		String[] hobbies = request.getParameterValues("rhobby");
		String chk ="";
		
		// 체크박스를 체크 했다면,
		if(hobbies != null) {
			for(int i=0;i<hobbies.length;i++) {
				chk = chk+hobbies[i]+" ";
			}
			dto.setRhobby(chk);
		}else {
			dto.setRhobby(" ");
		}
		
		dao.updateDao(dto); // update 실행
		return "redirect:list"; //WEB-INF/views/list.jsp
	}
}

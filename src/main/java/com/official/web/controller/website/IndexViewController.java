package com.official.web.controller.website;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.official.foundation.facade.product.ClassifyFacadeService;
import com.official.web.controller.BaseController;

@Controller
public class IndexViewController extends BaseController {
	
	@Autowired
	private ClassifyFacadeService classifyFacadeService;

	@RequestMapping(value = { "/index", "/" })
	public ModelAndView toIndex(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/index");
		view.addObject("data", classifyFacadeService.findAll());
		return view;
	}

}

package com.official.web.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.official.core.entity.LoginUser;
import com.official.core.util.Commutil;
import com.official.core.util.LoginUtils;
import com.official.foundation.domain.po.product.Classify;
import com.official.foundation.facade.product.ClassifyFacadeService;
import com.official.foundation.facade.product.CommodityFacadeService;

@Controller
@RequestMapping("/admin/product")
public class ProductViewController extends BaseController {

	@Autowired
	private CommodityFacadeService commodityService;

	@Autowired
	private ClassifyFacadeService classifyService;

	@RequestMapping("/commodity.htm")
	public ModelAndView commodityView(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/product/commodity");
		view.addObject("title", "产品列表");
		String msg = request.getParameter("msg");
		view.addObject("msg", msg);
		return view;
	}

	/**
	 * 获取产品数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/commodityData.htm", method = { RequestMethod.POST })
	public Map<String, Object> commodityData(HttpServletRequest request, Integer pageIndex, Integer pageSize,
			String keyWords) {
		return null;
	}

	@RequestMapping(value = "/addCommodit.htm")
	public ModelAndView addCommodit(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/product/addCommodity");
		view.addObject("title", "添加产品");
		return view;
	}

	/**
	 * 产品分类
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/classify.htm")
	public ModelAndView classifyView(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/product/classifyList");
		String msg = request.getParameter("msg");
		view.addObject("msg", msg);
		view.addObject("title", "分类列表");
		Integer pageIndex = Commutil.null2Int(request.getParameter("pageIndex"), 0);
		Integer pageSize = Commutil.null2Int(request.getParameter("pageSize"), 200);
		String title = Commutil.null2String(request.getParameter("title"));
		Classify classify = new Classify();
		if (StringUtils.isNotBlank(title)) {
			classify.setTitle(title);
		}
		Map<String, Object> data = this.classifyService.searchClassify(classify, pageIndex, pageSize);
		view.addObject("data", data);
		return view;
	}

	/**
	 * 获取分类数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/classifyData.htm", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> classifyData(HttpServletRequest request) {
		Integer pageIndex = Commutil.null2Int(request.getParameter("pageIndex"), 0);
		Integer pageSize = Commutil.null2Int(request.getParameter("pageSize"), 20);
		String title = Commutil.null2String(request.getParameter("title"));
		Classify classify = new Classify();
		if (StringUtils.isNotBlank(title)) {
			classify.setTitle(title);
		}
		Map<String, Object> data = this.classifyService.searchClassify(classify, pageIndex, pageSize);
		return data;
	}

	/**
	 * 添加分类页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/addClassifyView.htm")
	public ModelAndView addClassifyView(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/product/classifyAdd");
		view.addObject("title", "添加分类");
		return view;
	}
	/**
	 * 修改产品
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/editClassify.htm", method = { RequestMethod.POST })
	public ModelAndView updateClassifyView(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		Long id=Commutil.null2Long(request.getParameter("id"));
		Classify entity=this.classifyService.findOne(id);
		if(entity==null){
			view.setViewName("redirect:/admin/product/classify.htm");
			return view;
		}
		view.addObject("classify", entity);
		view.setViewName("/product/classifyAdd");
		view.addObject("title", "修改分类");
		return view;
	}
	/**
	 * 删除产品
	 * @param request
	 * @param attr
	 * @return
	 */
	@RequestMapping(value="/deleteClassify.htm", method = { RequestMethod.POST })
	public String deleteClassify(HttpServletRequest request, RedirectAttributes attr){
		Long id=Commutil.null2Long(request.getParameter("id"));
		Classify entity=this.classifyService.findOne(id);
		if(entity==null){
			attr.addFlashAttribute("msg", "删除失败!该分类不存在");
		}else{
			entity.setDeleteStatus(true);
			this.classifyService.save(entity);
			attr.addFlashAttribute("msg", "删除成功");
		}
		return "redirect:/admin/product/classify.htm";
	}
	
	@RequestMapping(value = "/addClassify.htm", method = { RequestMethod.POST })
	public String addClassify(HttpServletRequest request, RedirectAttributes attr) {
		LoginUser lu = LoginUtils.getCurrentuser(request);
		Classify classify = new Classify();
		Long id=Commutil.null2Long(request.getParameter("id"));
		if(id>0){
			Classify entity=this.classifyService.findOne(id);
			if(entity!=null){
				classify=entity;
				classify.setUpdateBy(lu.getUsername());
				classify.setUpdateTime(new Date());
			}
		}
		if(StringUtils.isBlank(classify.getUpdateBy())){
			classify.setCreateBy(lu.getUsername());
			classify.setCreateTime(new Date());
		}
		String title = request.getParameter("title");
		String keywords = request.getParameter("keywords");
		String description = request.getParameter("description");
		String remarks = request.getParameter("remarks");
		Integer weight=Commutil.null2Int(request.getParameter("weight"),0);
		classify.setTitle(title);	
		classify.setDescription(description);
		classify.setKeywords(keywords);
		classify.setState(0);
		classify.setTitle(title);
		classify.setRemarks(remarks);
		classify.setWeight(weight);
		this.classifyService.save(classify);
		attr.addFlashAttribute("msg", "添加成功");
		return "redirect:/admin/product/classify.htm";
	}

}

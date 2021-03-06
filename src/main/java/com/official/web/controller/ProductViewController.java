package com.official.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.official.core.entity.LoginUser;
import com.official.core.util.Binding;
import com.official.core.util.Commutil;
import com.official.core.util.GainCallback;
import com.official.core.util.LoginUtils;
import com.official.foundation.domain.po.product.Classify;
import com.official.foundation.domain.po.product.Commodity;
import com.official.foundation.domain.po.product.Specification;
import com.official.foundation.facade.product.ClassifyFacadeService;
import com.official.foundation.facade.product.CommodityFacadeService;
import com.official.foundation.facade.product.SpecificationFacadeService;

@Controller
@RequestMapping("/admin/product")
public class ProductViewController extends BaseController {

	@Autowired
	private CommodityFacadeService commodityService;

	@Autowired
	private ClassifyFacadeService classifyService;

	@Autowired
	private SpecificationFacadeService specificationService;
	
	
	
	
	/**
	 * 保存规格
	 * 
	 * @param request
	 */
	public void saveSpec(HttpServletRequest request,String titles) {
		LoginUser lu = LoginUtils.getCurrentuser(request);
		String[] arr = titles.split(",");
		for (String title : arr) {
			if(StringUtils.isBlank(title)){
				continue;
			}
			String type="spec";
			String firstWord = Commutil.TransformationPy(title, Commutil.PINYIN_TRANSFORMATION_FIRST_ONE_CAPITAL);
			Specification spec = new Specification();
			spec.setType(type);
			spec.setTitle(title);
			spec.setFirstWord(firstWord);
			spec.setStoreId(lu.getId());
			spec.setCreateTime(new Date());
			spec.setCreateBy(lu.getUsername());
			spec.setDeleteStatus(false);
			boolean flag = this.specificationService.checkTitle(title);
			if (!flag) {
				this.specificationService.save(spec);
			}
		}
	}

	/**
	 * 获取已经填写过的规格
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/specList.htm", method = { RequestMethod.GET })
	@ResponseBody
	public List<Specification> getSpec(HttpServletRequest request) {
		LoginUser lu = LoginUtils.getCurrentuser(request);
		Specification spec = new Specification();
		spec.setStoreId(lu.getId());
		return this.specificationService.searchSpec(spec);
	}
	
	

	/**
	 * 产品列表
	 * 
	 * @param request
	 * @return
	 */
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
	@RequestMapping(value = "/commodityData.htm", method = { RequestMethod.POST,RequestMethod.GET   })
	@ResponseBody
	public Map<String, Object> commodityData(HttpServletRequest request) {
		String keyWords=request.getParameter("keyWords");
		Integer pageIndex=Commutil.null2Int(request.getParameter("pageIndex"), 0);
		Integer pageSize=Commutil.null2Int(request.getParameter("pageSize"), 20);
		Commodity commodity=new Commodity();
		if(StringUtils.isNotBlank(keyWords)){
			commodity.setTitle(keyWords);
		}
		return this.commodityService.searchCommodity(commodity, pageIndex, pageSize);
	}
	/**
	 * 添加产品
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/editCommodity.htm", method = { RequestMethod.POST,RequestMethod.GET})
	public ModelAndView editCommodity(HttpServletRequest request) {
		LoginUser lu = LoginUtils.getCurrentuser(request);
		ModelAndView view = new ModelAndView();
		Long id=Commutil.null2Long(request.getParameter("id"));
		Commodity commodity=this.commodityService.findOne(id);
		view.addObject("commodity", commodity);
		view.setViewName("/product/addCommodity");
		view.addObject("title", "修改产品");
		Classify classify = new Classify();
		classify.setCreateBy(lu.getUsername());
		List<Classify> data = this.classifyService.searchClassify(classify);
		view.addObject("classifyList", data);
		return view;
	}
	
	@RequestMapping(value = "/showCommodity.htm", method = { RequestMethod.POST,RequestMethod.GET   })
	public ModelAndView showCommodity(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		Long id=Commutil.null2Long(request.getParameter("id"));
		Commodity commodity=this.commodityService.findOne(id);
		view.addObject("commodity", commodity);
		view.setViewName("/product/showCommodity");
		return view;
	}
	
	@RequestMapping(value = "/deleteCommodity.htm", method = { RequestMethod.POST,RequestMethod.GET   })
	public String delete(HttpServletRequest request,RedirectAttributes attr){
		Long id=Commutil.null2Long(request.getParameter("id"));
		this.commodityService.delete(id);
		attr.addFlashAttribute("msg", "删除成功");
		return "redirect:/admin/product/commodity.htm";
	}
	/**
	 * 添加产品页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addCommoditView.htm")
	public ModelAndView addCommoditView(HttpServletRequest request) {
		LoginUser lu = LoginUtils.getCurrentuser(request);
		ModelAndView view = new ModelAndView();
		view.setViewName("/product/addCommodity");
		view.addObject("title", "添加产品");
		Classify classify = new Classify();
		classify.setCreateBy(lu.getUsername());
		List<Classify> data = this.classifyService.searchClassify(classify);
		view.addObject("classifyList", data);
		return view;
	}
	/**
	 * 添加产品
	 * @param request
	 * @param attr
	 * @param multipartFile
	 * @return
	 */
	@RequestMapping(value = "/addCommodit.htm", method = { RequestMethod.POST })
	public String addCommodit(HttpServletRequest request, RedirectAttributes attr,
			@RequestParam("file") MultipartFile multipartFile) {
		final CommodityFacadeService service=this.commodityService;
		Commodity commodity=Binding.analysis(request, Commodity.class, new GainCallback<Commodity>(){
			@Override
			public Commodity findOne(Long id) {	
				return service.findOne(id);
			}		
		});
		service.save(commodity);
		System.out.println(commodity);
		
		return "redirect:/admin/product/commodity.htm";
	}

	/**
	 * 产品分类
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/classify.htm")
	public ModelAndView classifyView(HttpServletRequest request) {
		LoginUser lu = LoginUtils.getCurrentuser(request);
		ModelAndView view = new ModelAndView();
		view.setViewName("/product/classifyList");
		String msg = request.getParameter("msg");
		view.addObject("msg", msg);
		view.addObject("title", "分类列表");
		String title = Commutil.null2String(request.getParameter("title"));
		Classify classify = new Classify();
		classify.setCreateBy(lu.getUsername());
		if (StringUtils.isNotBlank(title)) {
			classify.setTitle(title);
		}
		List<Classify> data = this.classifyService.searchClassify(classify);
		view.addObject("data", data);
		return view;
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
	 * 修改分类
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/editClassify.htm", method = { RequestMethod.POST })
	public ModelAndView updateClassifyView(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		Long id = Commutil.null2Long(request.getParameter("id"));
		Classify entity = this.classifyService.findOne(id);
		if (entity == null) {
			view.setViewName("redirect:/admin/product/classify.htm");
			return view;
		}
		view.addObject("classify", entity);
		view.setViewName("/product/classifyAdd");
		view.addObject("title", "修改分类");
		return view;
	}

	/**
	 * 删除分类
	 * 
	 * @param request
	 * @param attr
	 * @return
	 */
	@RequestMapping(value = "/deleteClassify.htm", method = { RequestMethod.POST })
	public String deleteClassify(HttpServletRequest request, RedirectAttributes attr) {
		Long id = Commutil.null2Long(request.getParameter("id"));
		Classify entity = this.classifyService.findOne(id);
		if (entity == null) {
			attr.addFlashAttribute("msg", "删除失败!该分类不存在");
		} else {
			entity.setDeleteStatus(true);
			this.classifyService.save(entity);
			attr.addFlashAttribute("msg", "删除成功");
		}
		return "redirect:/admin/product/classify.htm";
	}

	/**
	 * 添加分类
	 * 
	 * @param request
	 * @param attr
	 * @return
	 */
	@RequestMapping(value = "/addClassify.htm", method = { RequestMethod.POST })
	public String addClassify(HttpServletRequest request, RedirectAttributes attr) {
		LoginUser lu = LoginUtils.getCurrentuser(request);
		Classify classify = new Classify();
		Long id = Commutil.null2Long(request.getParameter("id"));
		if (id > 0) {
			Classify entity = this.classifyService.findOne(id);
			if (entity != null) {
				classify = entity;
				classify.setUpdateBy(lu.getUsername());
				classify.setUpdateTime(new Date());
			}
		}
		if (StringUtils.isBlank(classify.getUpdateBy())) {
			classify.setCreateBy(lu.getUsername());
			classify.setCreateTime(new Date());
		}
		String title = request.getParameter("title");
		String keywords = request.getParameter("keywords");
		String description = request.getParameter("description");
		String remarks = request.getParameter("remarks");
		Integer weight = Commutil.null2Int(request.getParameter("weight"), 0);
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

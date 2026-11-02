package th.co.wacoal.atech.pcms2.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = { "/SapToWeb" })
public class SapToWebController {
	@RequestMapping(method = { RequestMethod.GET })
	public ModelAndView getModelAndView(HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Test/SapToWeb");
		return mv;
	}

}

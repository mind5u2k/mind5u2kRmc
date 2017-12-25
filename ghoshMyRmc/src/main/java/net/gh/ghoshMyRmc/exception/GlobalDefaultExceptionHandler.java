package net.gh.ghoshMyRmc.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

 
public class GlobalDefaultExceptionHandler {

	/**
	 * @author anurag ghosh
	 */

	@ExceptionHandler(NoHandlerFoundException.class)
	public ModelAndView handlerNoHandlerFoundException() {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("errorTitle", "This Page is not Constructed");
		mv.addObject("errorDescription",
				"The Page you are looking for is not available yet !!");
		mv.addObject("title", "404 Error Page");
		return mv;
	}

	@ExceptionHandler(NotFoundException.class)
	public ModelAndView notFoundException() {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("errorTitle", "Something Went Wrong");
		mv.addObject("errorDescription", "!! Please try to Login again !!");
		mv.addObject("title", "oopssss!!!");
		return mv;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException() {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("errorTitle", "Something Went Wrong");
		mv.addObject("errorDescription", "!! Please try to Login again !!");
		mv.addObject("title", "oopsss !!!");
		return mv;
	}
}

package com.irene.animations.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
	@Autowired
	private ResourceLoader resourceLoader;

	@GetMapping("/")
	public ModelAndView home(ModelAndView mav) {
		
		mav.setViewName("default/index");
		mav.addObject("templates", getTemplateNames());
		return mav;
	}

	private Collection<String> getTemplateNames() {

		List<String> resources = new ArrayList<>();
		try {
			final String root = "\\templates\\layouts\\";
			final String filter = String.format("classpath:%s**", root);
			for (Resource r :ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(filter)) {
				if (r.getFile().isDirectory()) {
					File index = new File(r.getFile(), "index.html");
					if (index.exists()) {
						String templateFolder = index.getParentFile().getAbsolutePath();
						String templateName = templateFolder.substring(templateFolder.indexOf(root) + root.length());
						resources.add(templateName);
					}
				}
			}
		} catch (IOException ex) {
			System.err.println(ex.toString());
		}
		
		return resources;
	}
	
	@GetMapping("/{url}")
	public ModelAndView template(@PathVariable String url) {
		return new ModelAndView("layouts/"+url+"/index");
	}

}

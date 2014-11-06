package net.foound;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import net.foound.filter.CustomFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan(basePackages = { "net.foound" })
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter
{
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private Environment environment;
	
	public static final String APP_NAME = "Foound";
	public static final String APP_VERSION = "1.0.0";
	public static final String VIEW_RESOLVER_PREFIX = "/WEB-INF/view/";
	public static final String VIEW_RESOLVER_SUFFIX = ".jsp";
	public static final String LAYOUT_RESOLVER_PREFIX = "/WEB-INF/layout/";
	public static final String LAYOUT_RESOLVER_SUFFIX = ".jsp";
	
	private static ServletContext globalServletContext;
	private static Environment globalEnvironment;
	
	public static final ServletContext getServletContext()
	{
		return globalServletContext;
	}
	
	public static final Environment getEnvironment()
	{
		return globalEnvironment;
	}
	
	@PostConstruct
	public void contextInitialized()
	{
		globalServletContext = servletContext;
		
		globalEnvironment = environment;
	}
	
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer)
	{
		configurer.setDefaultTimeout(30 * 1000L);
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
	{
		configurer.enable();
	}
	
	@Bean
	public OncePerRequestFilter configureOncePerRequestFilter()
	{
		return new CustomFilter();
	}
	
	@Bean
	public MultipartResolver configureMultipartResolver()
	{
		return new CommonsMultipartResolver();
	}
	
	@Bean
	public MessageSource configureMessageSource()
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setCacheSeconds(5);
		messageSource.setDefaultEncoding("UTF-8");
		
		return messageSource;
	}
	
	@Bean
	public ViewResolver configureViewResolver()
	{
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		
		return internalResourceViewResolver;
	}
	
	// @Bean
	// public ViewResolver configureViewResolver(ResourceLoader resourceLoader)
	// {
	// MustacheTemplateLoader mustacheTemplateLoader = new
	// MustacheTemplateLoader();
	// mustacheTemplateLoader.setResourceLoader(resourceLoader);
	//
	// MustacheViewResolver mustacheViewResolver = new MustacheViewResolver();
	// mustacheViewResolver.setTemplateLoader(mustacheTemplateLoader);
	//
	// return mustacheViewResolver;
	// }
	
	// @Override
	// public void addViewControllers(ViewControllerRegistry registry) {
	//
	// super.addViewControllers(registry);
	//
	// registry.addViewController("login").setViewName("login");
	// }
	
	// @Bean
	// public ViewResolver setupTilesViewResolver() {
	//
	// TilesViewResolver tilesViewResolver = new TilesViewResolver();
	//
	// return tilesViewResolver;
	// }
	
	// @Bean
	// public TilesConfigurer setupTilesConfigurer() {
	//
	// TilesConfigurer tilesConfigurer = new TilesConfigurer();
	// tilesConfigurer.setDefinitions("/WEB-INF/tile-defs/templates.xml",
	// "/WEB-INF/tile-defs/definitions.xml");
	//
	// return tilesConfigurer;
	// }
	
	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	//
	// registry.addResourceHandler("/resources/**").addResourceLocations(
	// "/resources/");
	// }
	
	// @Bean
	// public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
	//
	// Properties mappings = new Properties();
	// mappings.put("org.springframework.dao.DataAccessException", "error");
	//
	// SimpleMappingExceptionResolver simpleMappingExceptionResolver = new
	// SimpleMappingExceptionResolver();
	// simpleMappingExceptionResolver.setExceptionMappings(mappings);
	//
	// return simpleMappingExceptionResolver;
	// }
}
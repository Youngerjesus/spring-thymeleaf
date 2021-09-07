# Spring Thymeleaf 

> __Thymeleaf 가 무엇이고 어떠한 기능을 제공해주는지 알아보고, 스프링에서 Thymeleaf 를 가지고 어떻게 뷰를 만드는지 알아보기 위해 정리한 글입니다. __
> 
> __ 정리할 내용은 다음과 같습니다.__ 
> - Thymeleaf 의 목적
> - Thymeleaf 가 제공해주는 Template 
> - Spring Boot 에서 Thymeleaf 를 이용하기 위한 설정
> - Thymeleaf 가 스프링에서 동작하는 과정
>
> __References__
> - [Thymeleaf Document](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
> - [thymeleaf-spring5 3.0.9.RELEASE API](https://www.thymeleaf.org/apidocs/thymeleaf-spring5/3.0.9.RELEASE/)
> - [Spring Web Mvc Document](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/) 

***

## Thymeleaf 의 목적 

Thymeleaf 는 자바에서 server-side 렌더링을 지원해주는 Java Template Engine 이다. 

Thymeleaf 의 주요 목적은 유지 관리가 수월한 템플릿을 작성하도록 지원하는 것이다. 

이는 Thymeleaf 의 핵심 기능인 _**Natural Template**_ 을 통해 제공해주는데 Natural Template 은 서버 사이드 렌더링을 하는데 필요한 데이터가 없더라도 프로토 타입으로서의 역할을 해줄 수 있는 걸 말한다. 

이 특징이 기존 Java Template Engine 중의 하나인 Jsp 와 가장 다른 점인데 Jsp 는 화면을 보기 위해선 서버의 도움이 필요하다. 그치만 Thymeleaf 는 서버의 도움없이 프로토 타입 형태로도 뷰를 볼 수 있다. 

그렇기 때문에 Thymeleaf 를 사용한다면 디자인팀과 개발팀 사이에 생길 수 있는 커뮤니케이션 비용을 줄여줄 수 있다. 

***

## Thymleaf 가 제공해주는 Template 

타임리프는 다음과 같은 6 가지의 템플릿을 제공해준다. 

- HTML

- XML

- TEXT

- Javascript

- Css 

- Raw 

정리하면 Thymeleaf 는 2 개의 makup Template Mode (HTML and XML) 가 있고 3 개의 Textual Template Mode (TEXT, Javascript and Css) 가 있고 하나의 no-op Template Mode (Raw) 가 있다. 

***

## Spring Boot 에서 Thymeleaf 를 이용하기 위한 설정 

타임리프를 가장 빠르게 사용하는 방법은 Maven 이나 Gradle 같은 빌드 툴을 이용하면 된다. 

Spring Boot 의 Maven 기준으로 [Maven Central Repository](https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf) 에 접근해서 다음과 같이 각 사용할 버전을 가지고 오면된다. 

```xml
<dependency>
  <groupId>org.thymeleaf</groupId>
  <artifactId>thymeleaf</artifactId>
  <version>3.0.12.RELEASE</version>
</dependency>
```

Spring Boot 에서 Thymeleaf 의 동작 과정을 살펴보기 위해서 [Github Repository](https://github.com/Youngerjesus/spring-thymeleaf) 를 만들었는데 참고해도 좋다. 

***

## Thymeleaf 가 스프링에서 동작하는 과정 

Spring Boot 에서 Thymeleaf 뷰를 클라이언트에서 요청하면 동적 페이지를 만드는 과정이 어떻게 이뤄지는지 핵심 컴포넌트 별로 정리해봤다. 

### 1. DispatcherServlet

잘 알고 있겠지만 클라인트에게서 온 HTTP 기반의 요청은 모두 DispatcherServlet 을 지나친다. (이것을 프론트 컨트롤러 패턴이라고도 한다.)  

DispatcherServlet 은 HandlerMapping 전략에 따라서 요청을 핸들링 해줄 핸들러를 선택해준다. 

기본적으로 Spring MVC 에서 제공해주는 HandlerMapping 구현체로는 BeanNameUrlHandlerMapping 과 RequestMappingHandler 가 있다. 

조금만 부연설명을 하자면 BeanNameUrlHandlerMapping 은 요청 URL 에서 / 다음에 오는 문자열과 매칭이 되는 빈 이름이 있다면 그 빈이 요청을 처리해주는 핸들러로 선택이 되는 전략이고  

RequestMappingHandler 는 @Controller 애노테이션에 같이 붙는 @RequestMapping 애노테이션의 URL 과 매칭이되는 해당 컨트롤러 빈이 있다면 요청을 처리할 핸들러 역할을 하도록 한다. 

HandlerMapping 전략에 따라서 적합한 핸들러를 선택했다면 요청을 이제 각 핸들러에게 넘겨주고 실행을 하게한다. 이를 위해서 Adapter 패턴이 이용되고 HandlerAdapter 를 통해서 각 핸들러가 실행이 된다. 

DispatcherServlet 은 이런 HandlerAdapter 인터페이스를 통해서 핸들러에 접근할 수 있다.  

HandlerAdapter 를 통해서 요청을 처리하고 나면 리턴되는 결과물로 ModelAndView 를 받는다. ModelAndView 는 MVC Framework 의 Model 과 View 를 모두 가지고 있는 객체라는 뜻으로 알면된다. 

Model 이 View 에 렌더링 될 데이터를 뜻하는 거라면 View 는 클라이언트가 볼 뷰 페이지의 이름을 말한다. 

이렇게 ModelAndView 값이 NULL 이 아닌 값으로 반환이 된다면 이 값을 바탕으로 DispatcherServlet 은 기본적으로 가지고 있는 ContentNegotiatingViewResolver 를 통해서 이를 처리해줄 수 있는 적합한 최적의 View 를 찾는다. (ViewResolver 는 View 값과 Locale 값을 바탕으로 렌더링을 할 View 객체를 찾는 역할은 한다.)

여기서 Thymeleaf 를 사용한다면 ThymeleafView 값이 리턴이 되고 ThymeleafView 객체의 render() 메소드를 통해서 주어진 Model 을 가지고 동적 페이지를 만드는 렌더링 과정을 시작한다. 

즉 이 과정을 정리하자면 DispatcherServlet 에서 호출한 메소드의 순서는 다음과 같다. 코드도 추가로 첨부하겠다. 

#### 1. DispatcherServlet.doDispatch()

```java
@SuppressWarnings("deprecation")
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpServletRequest processedRequest = request;
    HandlerExecutionChain mappedHandler = null;
    boolean multipartRequestParsed = false;

    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

    try {
        ModelAndView mv = null;
        Exception dispatchException = null;

        try {
            processedRequest = checkMultipart(request);
            multipartRequestParsed = (processedRequest != request);

            // Determine handler for the current request.
            mappedHandler = getHandler(processedRequest);
            if (mappedHandler == null) {
                noHandlerFound(processedRequest, response);
                return;
            }

            // Determine handler adapter for the current request.
            HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

            // Process last-modified header, if supported by the handler.
            String method = request.getMethod();
            boolean isGet = HttpMethod.GET.matches(method);
            if (isGet || HttpMethod.HEAD.matches(method)) {
                long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
                    return;
                }
            }

            if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                return;
            }

            // Actually invoke the handler.
            mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

            if (asyncManager.isConcurrentHandlingStarted()) {
                return;
            }

            applyDefaultViewName(processedRequest, mv);
            mappedHandler.applyPostHandle(processedRequest, response, mv);
        }
        catch (Exception ex) {
            dispatchException = ex;
        }
        catch (Throwable err) {
            // As of 4.3, we're processing Errors thrown from handler methods as well,
            // making them available for @ExceptionHandler methods and other scenarios.
            dispatchException = new NestedServletException("Handler dispatch failed", err);
        }
        processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
    }
    catch (Exception ex) {
        triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
    }
    catch (Throwable err) {
        triggerAfterCompletion(processedRequest, response, mappedHandler,
                new NestedServletException("Handler processing failed", err));
    }
    finally {
        if (asyncManager.isConcurrentHandlingStarted()) {
            // Instead of postHandle and afterCompletion
            if (mappedHandler != null) {
                mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
            }
        }
        else {
            // Clean up any resources used by a multipart request.
            if (multipartRequestParsed) {
                cleanupMultipart(processedRequest);
            }
        }
    }
}
``` 

#### 2. processDispatchResult() 

```java
/**
 * Handle the result of handler selection and handler invocation, which is
 * either a ModelAndView or an Exception to be resolved to a ModelAndView.
 */
private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
        @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
        @Nullable Exception exception) throws Exception {

    boolean errorView = false;

    if (exception != null) {
        if (exception instanceof ModelAndViewDefiningException) {
            logger.debug("ModelAndViewDefiningException encountered", exception);
            mv = ((ModelAndViewDefiningException) exception).getModelAndView();
        }
        else {
            Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
            mv = processHandlerException(request, response, handler, exception);
            errorView = (mv != null);
        }
    }

    // Did the handler return a view to render?
    if (mv != null && !mv.wasCleared()) {
        render(mv, request, response);
        if (errorView) {
            WebUtils.clearErrorRequestAttributes(request);
        }
    }
    else {
        if (logger.isTraceEnabled()) {
            logger.trace("No view rendering, null ModelAndView returned.");
        }
    }

    if (WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
        // Concurrent handling started during a forward
        return;
    }

    if (mappedHandler != null) {
        // Exception (if any) is already handled..
        mappedHandler.triggerAfterCompletion(request, response, null);
    }
}
```

#### 3. render() 

````java
/**
 * Render the given ModelAndView.
 * <p>This is the last stage in handling a request. It may involve resolving the view by name.
 * @param mv the ModelAndView to render
 * @param request current HTTP servlet request
 * @param response current HTTP servlet response
 * @throws ServletException if view is missing or cannot be resolved
 * @throws Exception if there's a problem rendering the view
 */
protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
    // Determine locale for request and apply it to the response.
    Locale locale =
            (this.localeResolver != null ? this.localeResolver.resolveLocale(request) : request.getLocale());
    response.setLocale(locale);

    View view;
    String viewName = mv.getViewName();
    if (viewName != null) {
        // We need to resolve the view name.
        view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
        if (view == null) {
            throw new ServletException("Could not resolve view with name '" + mv.getViewName() +
                    "' in servlet with name '" + getServletName() + "'");
        }
    }
    else {
        // No need to lookup: the ModelAndView object contains the actual View object.
        view = mv.getView();
        if (view == null) {
            throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a " +
                    "View object in servlet with name '" + getServletName() + "'");
        }
    }

    // Delegate to the View object for rendering.
    if (logger.isTraceEnabled()) {
        logger.trace("Rendering view [" + view + "] ");
    }
    try {
        if (mv.getStatus() != null) {
            response.setStatus(mv.getStatus().value());
        }
        view.render(mv.getModelInternal(), request, response);
    }
    catch (Exception ex) {
        if (logger.isDebugEnabled()) {
            logger.debug("Error rendering view [" + view + "]", ex);
        }
        throw ex;
    }
}
````

### 2. ThymeleafView 

DispatcherServlet 에서 ContentNegotiationViewResolver 를 통해 ThymeleafView 객체를 찾았고 찾은 Thymeleaf 객체를 통해 동적 렌더링을 시작한다. 

ThymeleafView 객체는 렌더링을 하기 위해 여러가지 정보들을 가지고 있는 Context 객체를 만든다. 

이런 Context 객체 안에는 Model 을 정보 뿐 아니라 Locale 과 관련된 정보, Thymeleaf Template 파일을 찾기 위한 TemplateResolver , Request 와 Response 같은 정보도 포함하고 있다. 

Context 객체를 만든 후에는 이제 Thymeleaf 의 Template 을 찾고 Parsing 하면서 실제로 동적으로 렌더링을 해주는 작업을 해달라는 요청을TemplateEngine 에게 보낸다. 이 작업은 TemplateEngine 의 process() 메소드들 통해서 이뤄진다. 

정리하면 ThymeleafView 에서 호출한 메소드의 순서는 다음과 같다. 

#### 1. ThymeleafView.render() 

```java
public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
    renderFragment(this.markupSelectors, model, request, response);
}
```

#### 2. ThymeleafView.renderFragment() 

```java
protected void renderFragment(final Set<String> markupSelectorsToRender, final Map<String, ?> model, final HttpServletRequest request,
            final HttpServletResponse response)
            throws Exception {

    final ServletContext servletContext = getServletContext() ;
    final String viewTemplateName = getTemplateName();
    final ISpringTemplateEngine viewTemplateEngine = getTemplateEngine();

    if (viewTemplateName == null) {
        throw new IllegalArgumentException("Property 'templateName' is required");
    }
    if (getLocale() == null) {
        throw new IllegalArgumentException("Property 'locale' is required");
    }
    if (viewTemplateEngine == null) {
        throw new IllegalArgumentException("Property 'templateEngine' is required");
    }

    final Map<String, Object> mergedModel = new HashMap<String, Object>(30);
    final Map<String, Object> templateStaticVariables = getStaticVariables();
    if (templateStaticVariables != null) {
        mergedModel.putAll(templateStaticVariables);
    }
    if (pathVariablesSelector != null) {
        @SuppressWarnings("unchecked")
        final Map<String, Object> pathVars = (Map<String, Object>) request.getAttribute(pathVariablesSelector);
        if (pathVars != null) {
            mergedModel.putAll(pathVars);
        }
    }
    if (model != null) {
        mergedModel.putAll(model);
    }

    final ApplicationContext applicationContext = getApplicationContext();

    final RequestContext requestContext =
            new RequestContext(request, response, getServletContext(), mergedModel);
    final SpringWebMvcThymeleafRequestContext thymeleafRequestContext =
            new SpringWebMvcThymeleafRequestContext(requestContext, request);

    // For compatibility with ThymeleafView
    addRequestContextAsVariable(mergedModel, SpringContextVariableNames.SPRING_REQUEST_CONTEXT, requestContext);
    // For compatibility with AbstractTemplateView
    addRequestContextAsVariable(mergedModel, AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, requestContext);
    // Add the Thymeleaf RequestContext wrapper that we will be using in this dialect (the bare RequestContext
    // stays in the context to for compatibility with other dialects)
    mergedModel.put(SpringContextVariableNames.THYMELEAF_REQUEST_CONTEXT, thymeleafRequestContext);


    // Expose Thymeleaf's own evaluation context as a model variable
    //
    // Note Spring's EvaluationContexts are NOT THREAD-SAFE (in exchange for SpelExpressions being thread-safe).
    // That's why we need to create a new EvaluationContext for each request / template execution, even if it is
    // quite expensive to create because of requiring the initialization of several ConcurrentHashMaps.
    final ConversionService conversionService =
            (ConversionService) request.getAttribute(ConversionService.class.getName()); // might be null!
    final ThymeleafEvaluationContext evaluationContext =
            new ThymeleafEvaluationContext(applicationContext, conversionService);
    mergedModel.put(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);


    final IEngineConfiguration configuration = viewTemplateEngine.getConfiguration();
    final WebExpressionContext context =
            new WebExpressionContext(configuration, request, response, servletContext, getLocale(), mergedModel);


    final String templateName;
    final Set<String> markupSelectors;
    if (!viewTemplateName.contains("::")) {
        // No fragment specified at the template name

        templateName = viewTemplateName;
        markupSelectors = null;

    } else {
        // Template name contains a fragment name, so we should parse it as such

        // A check must be made that the template name is not included in the URL, so that we make sure
        // no code to be executed comes from direct user input.
        SpringRequestUtils.checkViewNameNotInRequest(viewTemplateName, request);

        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);

        final FragmentExpression fragmentExpression;
        try {
            // By parsing it as a standard expression, we might profit from the expression cache
            fragmentExpression = (FragmentExpression) parser.parseExpression(context, "~{" + viewTemplateName + "}");
        } catch (final TemplateProcessingException e) {
            throw new IllegalArgumentException("Invalid template name specification: '" + viewTemplateName + "'");
        }

        final FragmentExpression.ExecutedFragmentExpression fragment =
                FragmentExpression.createExecutedFragmentExpression(context, fragmentExpression);

        templateName = FragmentExpression.resolveTemplateName(fragment);
        markupSelectors = FragmentExpression.resolveFragments(fragment);
        final Map<String,Object> nameFragmentParameters = fragment.getFragmentParameters();

        if (nameFragmentParameters != null) {

            if (fragment.hasSyntheticParameters()) {
                // We cannot allow synthetic parameters because there is no way to specify them at the template
                // engine execution!
                throw new IllegalArgumentException(
                        "Parameters in a view specification must be named (non-synthetic): '" + viewTemplateName + "'");
            }

            context.setVariables(nameFragmentParameters);

        }


    }


    final String templateContentType = getContentType();
    final Locale templateLocale = getLocale();
    final String templateCharacterEncoding = getCharacterEncoding();


    final Set<String> processMarkupSelectors;
    if (markupSelectors != null && markupSelectors.size() > 0) {
        if (markupSelectorsToRender != null && markupSelectorsToRender.size() > 0) {
            throw new IllegalArgumentException(
                    "A markup selector has been specified (" + Arrays.asList(markupSelectors) + ") for a view " +
                    "that was already being executed as a fragment (" + Arrays.asList(markupSelectorsToRender) + "). " +
                    "Only one fragment selection is allowed.");
        }
        processMarkupSelectors = markupSelectors;
    } else {
        if (markupSelectorsToRender != null && markupSelectorsToRender.size() > 0) {
            processMarkupSelectors = markupSelectorsToRender;
        } else {
            processMarkupSelectors = null;
        }
    }


    response.setLocale(templateLocale);

    if (!getForceContentType()) {

        final String computedContentType =
                SpringContentTypeUtils.computeViewContentType(
                        request,
                        (templateContentType != null? templateContentType : DEFAULT_CONTENT_TYPE),
                        (templateCharacterEncoding != null? Charset.forName(templateCharacterEncoding) : null));

        response.setContentType(computedContentType);

    } else {
        // We will force the content type parameters without trying to make smart assumptions over them

        if (templateContentType != null) {
            response.setContentType(templateContentType);
        } else {
            response.setContentType(DEFAULT_CONTENT_TYPE);
        }
        if (templateCharacterEncoding != null) {
            response.setCharacterEncoding(templateCharacterEncoding);
        }

    }

    final boolean producePartialOutputWhileProcessing = getProducePartialOutputWhileProcessing();

    // If we have chosen to not output anything until processing finishes, we will use a buffer
    final Writer templateWriter =
            (producePartialOutputWhileProcessing? response.getWriter() : new FastStringWriter(1024));

    viewTemplateEngine.process(templateName, processMarkupSelectors, context, templateWriter);

    // If a buffer was used, write it to the web server's output buffers all at once
    if (!producePartialOutputWhileProcessing) {
        response.getWriter().write(templateWriter.toString());
        response.getWriter().flush();
    }

}
```

### 3. TemplateEngine 

TemplateEngine 이 실제로 동적 페이지를 만드는 작업을 하지는 않는다. 이는 TemplateManager 가 수행하도록 되어있다. 

TemplateEngine 은 TemplateManager 가 이 작업을 하기 위해 빡센 객체 초기화 작업을 하고 여러가지 설정 정보들을 가지고 있는 역할을 한다. 

이중에는 Template 을 찾고 가져오기 위한 TemplateResovler 에 관련된 정보와 Template 처리 중에 확장해서 사용할 수 있는 기능인 Dialect 와 관련된 정보가 있고, 국제화를 위한 외부 메시지 같은 것들도 있다. 

외부 메시지는 MessageResolver 를 통해서 가능하고 기본적으로는 StandardMessageResolver 를 사용한다. 

그리고 TemplateEngine 을 사용할 때 몇가지 주의할 사항들이 있는데 여기서는 설명하지는 않고 링크만 남겨놓겠다. 

- [TemplateEngine (thymeleaf 3.0.0.BETA02 API)](https://www.thymeleaf.org/apidocs/thymeleaf/3.0.0.BETA02/org/thymeleaf/TemplateEngine.html)

- 여기서 말하는 주의사항 중에 TemplateEngine 의 인스턴스는 하나 만들어 놓고 재사용하는걸 권장하고 있는데 이유로는 인스턴스 생성 비용이 비싸서라고 한다. 추론인데 TemplateEngine 의 생성자에 ICacheManager 쪽에서 초기화 작업을 빡세게 하고 있어서 그런게 아닌가 생각하고 있다.  

TemplateEngine 이 TemplateManager 에게 작업을 맡기는 메소드는 다음과 같다.  

```java
public final void process(final TemplateSpec templateSpec, final IContext context, final Writer writer) {

    if (!this.initialized) {
        initialize();
    }
    
    try {
        
        Validate.notNull(templateSpec, "Template Specification cannot be null");
        Validate.notNull(context, "Context cannot be null");
        Validate.notNull(writer, "Writer cannot be null");
        // selectors CAN actually be null if we are going to render the entire template
        // templateMode CAN also be null if we are going to use the mode specified by the template resolver

        if (logger.isTraceEnabled()) {
            logger.trace("[THYMELEAF][{}] STARTING PROCESS OF TEMPLATE \"{}\" WITH LOCALE {}",
                    new Object[]{TemplateEngine.threadIndex(), templateSpec, context.getLocale()});
        }

        final long startNanos = System.nanoTime();

        final TemplateManager templateManager = this.configuration.getTemplateManager();
        templateManager.parseAndProcess(templateSpec, context, writer);

        final long endNanos = System.nanoTime();
        
        if (logger.isTraceEnabled()) {
            logger.trace("[THYMELEAF][{}] FINISHED PROCESS AND OUTPUT OF TEMPLATE \"{}\" WITH LOCALE {}",
                    new Object[]{TemplateEngine.threadIndex(), templateSpec, context.getLocale()});
        }

        if (timerLogger.isTraceEnabled()) {
            final BigDecimal elapsed = BigDecimal.valueOf(endNanos - startNanos);
            final BigDecimal elapsedMs = elapsed.divide(BigDecimal.valueOf(NANOS_IN_SECOND), RoundingMode.HALF_UP);
            timerLogger.trace(
                    "[THYMELEAF][{}][{}][{}][{}][{}] TEMPLATE \"{}\" WITH LOCALE {} PROCESSED IN {} nanoseconds (approx. {}ms)",
                    new Object[]{
                            TemplateEngine.threadIndex(),
                            LoggingUtils.loggifyTemplateName(templateSpec.getTemplate()), context.getLocale(), elapsed, elapsedMs,
                            templateSpec, context.getLocale(), elapsed, elapsedMs});
        }

        /*
         * Finally, flush the writer in order to make sure that everything has been written to output
         */
        try {
            writer.flush();
        } catch (final IOException e) {
            throw new TemplateOutputException("An error happened while flushing output writer", templateSpec.getTemplate(), -1, -1, e);
        }
        
    } catch (final TemplateOutputException e) {

        // We log the exception just in case higher levels do not end up logging it (e.g. they could simply display traces in the browser
        logger.error(String.format("[THYMELEAF][%s] Exception processing template \"%s\": %s", new Object[] {TemplateEngine.threadIndex(), templateSpec, e.getMessage()}), e);
        throw e;
        
    } catch (final TemplateEngineException e) {

        // We log the exception just in case higher levels do not end up logging it (e.g. they could simply display traces in the browser
        logger.error(String.format("[THYMELEAF][%s] Exception processing template \"%s\": %s", new Object[] {TemplateEngine.threadIndex(), templateSpec, e.getMessage()}), e);
        throw e;
        
    } catch (final RuntimeException e) {

        // We log the exception just in case higher levels do not end up logging it (e.g. they could simply display traces in the browser
        logger.error(String.format("[THYMELEAF][%s] Exception processing template \"%s\": %s", new Object[] {TemplateEngine.threadIndex(), templateSpec, e.getMessage()}), e);
        throw new TemplateProcessingException("Exception processing template", templateSpec.toString(), e);
        
    }
    
}
```


### 4. TemplateManager 

TemplateManager 의 parseAndProcess() 메소드를 통해서 동적 페이지인 뷰가 만들어진다. 

내부적으로 어떠한 일을 하는지 살펴보자. 

TemplateManager 는 이전에 Template 처리를 한 적이 있다면 LRU 기반의 캐싱 매커니즘을 통해 캐시 처리를 해서 바로 반환하도록 하고 

만약 캐싱되어 있지 않다면 TemplateResolver 의 resolveTemplate() 메소드를 통해 Template 이름을 바탕으로 경로에 있는 Template 을 가져오도록 한다. 

여기서 주의할 점은 가져왔다고 해서 Template 이 있는 건 아니다. 이는 TemplateResolver 의 구현에 따라서 다른데 어떠한 TemplateResovler 는 Performance 를 위해 존재하는 여부를 체크 안하는 경우도 있다. (체크를 한다면 두 번의 I/O 작업이 생겨서 그렇다.)

좀만 더 부연 설명을 하자면 여기서 Template 을 가지고 오는 역할을 하는건 최종적으로 Spring Core Class 에 있는 Resource 인터페이스의 구현체인 ClassPathResource 를 통해서 이뤄지고 Template 이 있는지 체크하는 메소드는 exist() 이다. 

그 후 실제로 Template 에 프로세싱 작업을 하기 위해 ProcessHandlerChain 을 만들고 각 Template 에 맞는 Parser 를 가져와서 Parsing 작업을 해준다. 즉 HTML Template 이라면 HTMLParser 를 가지고와서 파싱을 해준다. 

Parsing 작업이 끝나면 만들었던 ProcessHandlerChain 을 통해 View 를 만드는 작업을 끝낸다. 

TemplateManager 의 parseAndProcess() 메소드 구현을 살펴보면 다음과 같다. 

```java
 /*
 * -------------------------
 * PARSE-AND-PROCESS methods
 * -------------------------
 *
 * These methods perform the whole cycle of a template's processing: resolving, parsing and processing.
 * This is only meant to be called from the TemplateEngine
 */


public void parseAndProcess(
        final TemplateSpec templateSpec,
        final IContext context,
        final Writer writer) {

    Validate.notNull(templateSpec, "Template Specification cannot be null");
    Validate.notNull(context, "Context cannot be null");
    Validate.notNull(writer, "Writer cannot be null");


    // TemplateSpec will already have validated its contents, so need to do it here (template selectors,
    // resolution attributes, etc.)

    final String template = templateSpec.getTemplate();
    final Set<String> templateSelectors = templateSpec.getTemplateSelectors();
    final TemplateMode templateMode = templateSpec.getTemplateMode();
    final Map<String, Object> templateResolutionAttributes = templateSpec.getTemplateResolutionAttributes();

    final TemplateCacheKey cacheKey =
                new TemplateCacheKey(
                        null, // ownerTemplate
                        template, templateSelectors,
                        0, 0, // lineOffset, colOffset
                        templateMode,
                        templateResolutionAttributes);


    /*
     * First look at the cache - it might be already cached
     */
    if (this.templateCache != null) {

        final TemplateModel cached =  this.templateCache.get(cacheKey);

        if (cached != null) {

            final IEngineContext engineContext =
                    EngineContextManager.prepareEngineContext(this.configuration, cached.getTemplateData(), templateResolutionAttributes, context);

            /*
             * Create the handler chain to process the data.
             * This is PARSE + PROCESS, so its called from the TemplateEngine, and the only case in which we should apply
             * both pre-processors and post-processors (besides creating a last output-to-writer step)
             */
            final ProcessorTemplateHandler processorTemplateHandler = new ProcessorTemplateHandler();
            final ITemplateHandler processingHandlerChain =
                    createTemplateProcessingHandlerChain(engineContext, true, true, processorTemplateHandler, writer);

            cached.process(processingHandlerChain);

            EngineContextManager.disposeEngineContext(engineContext);

            return;

        }

    }


    /*
     * Resolve the template
     */
    final TemplateResolution templateResolution =
            resolveTemplate(this.configuration, null, template, templateResolutionAttributes, true);


    /*
     * Build the TemplateData object
     */
    final TemplateData templateData =
            buildTemplateData(templateResolution, template, templateSelectors, templateMode, true);


    /*
     * Prepare the context instance that corresponds to this execution of the template engine
     */
    final IEngineContext engineContext =
            EngineContextManager.prepareEngineContext(this.configuration, templateData, templateResolutionAttributes, context);


    /*
     * Create the handler chain to process the data.
     * This is PARSE + PROCESS, so its called from the TemplateEngine, and the only case in which we should apply
     * both pre-processors and post-processors (besides creating a last output-to-writer step)
     */
    final ProcessorTemplateHandler processorTemplateHandler = new ProcessorTemplateHandler();
    final ITemplateHandler processingHandlerChain =
            createTemplateProcessingHandlerChain(engineContext, true, true, processorTemplateHandler, writer);


    /*
     * Obtain the parser
     */
    final ITemplateParser parser = getParserForTemplateMode(engineContext.getTemplateMode());


    /*
     * If the resolved template is cacheable, so we will first read it as an object, cache it, and then process it
     */
    if (templateResolution.getValidity().isCacheable() && this.templateCache != null) {

        // Create the handler chain to create the Template object
        final ModelBuilderTemplateHandler builderHandler = new ModelBuilderTemplateHandler(this.configuration, templateData);

        // Process the template into a TemplateModel
        parser.parseStandalone(
                this.configuration,
                null, template, templateSelectors, templateData.getTemplateResource(),
                engineContext.getTemplateMode(), templateResolution.getUseDecoupledLogic(), builderHandler);

        // Obtain the TemplateModel
        final TemplateModel templateModel = builderHandler.getModel();

        // Put the new template into cache
        this.templateCache.put(cacheKey, templateModel);

        // Process the read (+cached) template itself
        templateModel.process(processingHandlerChain);

    } else {

        //  Process the template, which is not cacheable (so no worry about caching)
        parser.parseStandalone(
                this.configuration,
                null, template, templateSelectors, templateData.getTemplateResource(),
                engineContext.getTemplateMode(), templateResolution.getUseDecoupledLogic(),  processingHandlerChain);

    }


    /*
     * Dispose the engine context now that processing has been done
     */
    EngineContextManager.disposeEngineContext(engineContext);


}
```
 
***


## 코드 분석하면서 추가로 얻은 것 

### Document

- i.e 뜻은 "That is to say" 라는 말이다. "즉 다시 말해서"
- e.g 뜻은 "for example" 이라는 뜻이다.
- [i.g](http://i.ge) 와 e.g 둘 다 라틴어를 말한다.

### Dictionary

- Template
- Resolve
    - settle or find a solution

### Concurrency

- Two-Checked Mechanism

```java
// AbstractCacheManager 
if (!this.templateCacheInitialized) {
	synchronized(this) {
		// 한번 더 검사하는 로직. 
		if (!this.templateCacheInitialized) {
			this.templateCache = this.initializeTemplateCache(); 
			this.templateCacheInitialized = true;
		} 
	}
} 
```

### OOP

- Method 이름이 같은 것이 보인다.
    - TemplateManager.resolveTemplate() 과 TemplateResolver.resolveTemplate() 이렇게.
    - 아무래도 실제적인 작업을 수행하는건 TemplateResolver 지만 부가적인 작업들을 합쳐서 추상화해서 한번 더 감싸는듯.

### Cache

- LRU 캐시 메커니즘을 보고 싶다면 StandardCache 객체의 구현을 보면 될 듯.


### Chaining 

Spring MVC 에서는 ViewResolver 들을 Chaining 형식으로 서용하는데 다음과 같이 사용한다.

```java
if (this.viewResolvers != null) {
    for (ViewResolver viewResolver : this.viewResolvers) {
        View view = viewResolver.resolveViewName(viewName, locale);
        if (view != null) {
            return view; 
        }   
    }
}
``` 
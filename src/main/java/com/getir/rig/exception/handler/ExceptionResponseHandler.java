package com.getir.rig.exception.handler;

import com.getir.rig.exception.EmailAlreadyUsedException;
import com.getir.rig.exception.RecordNotFoundException;
import com.getir.rig.exception.RigGenericException;
import com.getir.rig.util.Constants;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionResponseHandler implements ProblemHandling, SecurityAdviceTrait, MessageSourceAware {

    private MessageSourceAccessor messages;

    @Override
    public ResponseEntity<Problem> process(ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return null;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }
        ProblemBuilder builder = Problem.builder()
                .withStatus(problem.getStatus())
                .withTitle(problem.getTitle())
                .with(Constants.ExceptionHandler.PATH_KEY, getPath(request));

        if (problem instanceof ConstraintViolationProblem) {
            builder
                    .with(Constants.ExceptionHandler.VIOLATIONS_KEY,
                            ((ConstraintViolationProblem) problem).getViolations())
                    .with(Constants.ExceptionHandler.MESSAGE_KEY,
                            this.messages.getMessage(Constants.ExceptionHandler.VIOLATIONS_MESSAGE_KEY));
        } else {
            if (problem.getDetail() != null) {
                builder.with(Constants.ExceptionHandler.LOCALIZED_MESSAGE_KEY, problem.getDetail());
            }
            problem.getParameters().forEach(builder::with);
        }

        final Object messageObject = problem.getParameters().get(Constants.ExceptionHandler.MESSAGE_KEY);
        if (messageObject instanceof String) {
            builder.with(Constants.ExceptionHandler.LOCALIZED_MESSAGE_KEY,
                    this.messages.getMessage((String) messageObject));
        }

        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEmailAlreadyUsedException(final EmailAlreadyUsedException ex,
                                                                   final NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withDetail(this.messages.getMessage(ex.getMessage()))
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleRecordNotFoundException(final RecordNotFoundException ex,
                                                                 final NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.NOT_FOUND)
                .withDetail(this.messages.getMessage(ex.getMessage()))
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleRigGenericException(final RigGenericException ex,
                                                             final NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .withDetail(this.messages.getMessage(ex.getMessage()))
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleException(final Exception ex, final NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .withDetail(this.messages.getMessage(ex.getMessage(),
                        this.messages.getMessage(Constants.ExceptionHandler.INTERNAL_SERVER_ERROR_MESSAGE_KEY,
                                Constants.ExceptionHandler.INTERNAL_SERVER_ERROR_DEFAULT_MESSAGE)))
                .build();
        return create(ex, problem, request);
    }

    private String getPath(final NativeWebRequest request) {
        try {
            return request.getNativeRequest(HttpServletRequest.class).getRequestURI();
        } catch (final Exception e) {
            //nothing to do
        }
        return Strings.EMPTY;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}


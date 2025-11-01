package com.wjc.codetest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
/*
* 문제:
* 1. 메서드가 증가할수록 @ResponseBody 중복되어 가독성 저하.
* 2. 전역 예외 처리기인데 value 값으로 특정 패키지로 제한하여 컨트롤러 추가 시 누락 위험이 있음.
*
* 개선안:
* 1. @RestControllerAdvice(@ControllerAdvice+@ResponseBody)를 사용하여 @ResponseBody 생략.
* 2. value를 생략해 모든 컨트롤러의 예외를 일관되게 처리.
* */
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
public class GlobalExceptionHandler {

    @ResponseBody
    /*
    * 문제:
    * 1. RuntimeException만 처리하고 있어 다른 예외처리 불가능.
    * 2. 무조건 HttpStatus.INTERNAL_SERVER_ERROR 500을 반환하여 다른 상태코드 반환 불가능.
    * 3. 로그 출력 시 e.getMessage()만 남겨 스택 트레이스가 기록되지 않아 원인 추적이 어려움.
    * 4. @ResponseStatus와 ResponseEntity.status() 같은 의미로 중복.
    *
    * 개선안:
    * 1. 예외 타입별로 세분화된 예외 처리 구현.
    * 2. 커스텀 예외별로 HTTP status 동적 설정.
    * 3. log.error 시 e 객체를 함께 전달해 스택 트레이스까지 로깅.
    *      log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "runtimeException",
                e.getMessage(),
                e // 추가
        );
    *  4. @ResponseStatus 제거하고 ResponseEntity로 상태 코드 관리.
    * */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> runTimeException(Exception e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "runtimeException",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

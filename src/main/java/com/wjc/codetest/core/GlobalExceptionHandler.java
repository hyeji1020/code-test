package com.wjc.codetest.core;

import com.wjc.codetest.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
    * 문제:
    * 1. RuntimeException만 처리하고 있어 세분화된 예외처리 불가능.
    * 2. 무조건 HttpStatus.INTERNAL_SERVER_ERROR 500을 반환하여 예외별 상태 코드 구분 불가능.
    * 3. 로그 출력 시 e.getMessage()만 남겨 스택 트레이스가 기록되지 않아 원인 추적이 어려움.
    * 4. @ResponseStatus와 ResponseEntity.status() 같은 의미로 중복.
    *
    * 개선안:
    * 1. 도메인별로 예외별로 구현하는것이 아닌(너무 많아지기 때문), 공통 예외 BaseException을 생성하여 상속받아 구현.
    * 2. ErrorCode를 enum으로 관리하여 한 곳에서 관리하고 에러 던질 때 ErrorCode만 지정하여 일관된 구조 사용.
    * ex) throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
    * 3. log.error 시 e 객체를 함께 전달해 스택 트레이스까지 로깅.
    * 4. @ResponseStatus 제거하고 ResponseEntity로 상태 코드 관리.
    *
     * 추가 고려사항:
     * 팀에서 성공·실패 시 공통 응답 포맷을 사용하기로 했다면,
     * - 별도의 CommonResponse 객체를 만들어 일관된 구조로 응답하도록 함.
     * - 다만, CommonResponse만 반환하면 성공 시 HTTP 상태 코드를 200으로만 반영하므로,
     *   ResponseEntity로 감싸 실제 상태 코드를 지정해야 함.
    * */

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleWantedException(BaseException ex) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                ex.getHttpStatus(),
                "BaseException",
                ex.getMessage()
        );
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(ex.getMessage());
    }

}

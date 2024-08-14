package com.todobuddy.backend.dto;

import com.todobuddy.backend.entity.MemoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "메모 상태(완료/미완료) 변경 Request DTO")
public class UpdateMemoStatusRequest {

    @NotNull(message = "변경하고자 하는 상태는 필수 입력 값입니다.")
    @Schema(description = "변경하고자 하는 상태\n"
        + " - (미완료 -> 변경) : COMPLETED)\n"
        + " - (완료 -> 미완료) : NOT_COMPLTED", example = "COMPLETED")
    private MemoStatus memoStatus;
}

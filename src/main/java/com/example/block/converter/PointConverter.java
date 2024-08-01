package com.example.block.converter;

import com.example.block.domain.PointDetail;
import com.example.block.domain.enums.PointType;
import com.example.block.dto.PointResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PointConverter {
    public static PointResponseDTO.GetMyPointDTO toPointDTO(Long point) {
        //포인트 조회 응답 정보 변환
        return PointResponseDTO.GetMyPointDTO.builder()
                .point(point)
                .build();
    }

    public static PointDetail toPointDetail(Long point, PointType type,String reason) {
        //포인트 상세 내역 생성
        return PointDetail.builder()
                .amount(point)
                .type(type)
                .reason(reason)
                .build();
    }

    public static PointResponseDTO.GetMyPointDetailDTO toPointDetailDTO(PointDetail pointDetail) {
        //포인트 상세 내역 조회 응답 정보 변환
        return PointResponseDTO.GetMyPointDetailDTO.builder()
                .amount(pointDetail.getAmount())
                .type(pointDetail.getType())
                .reason(pointDetail.getReason())
                .createdAt(pointDetail.getCreated_at())
                .build();
    }

    public static PointResponseDTO.GetMyPointDetailListDTO toPointDetailListDTO(List<PointDetail> pointDetailList) {
        //포인트 상세 내역 리스트 조회 응답 정보 변환
        List<PointResponseDTO.GetMyPointDetailDTO> pointDetailDTOList = pointDetailList.stream()
                .map(PointConverter::toPointDetailDTO)
                .collect(Collectors.toList()  );

        return PointResponseDTO.GetMyPointDetailListDTO.builder()
                .totalElements((long) pointDetailList.size())
                .detailList(pointDetailDTOList)
                .build();
    }

}

package shupship.service;

import shupship.dto.ResultResponse;
import shupship.request.ResultRequest;

public interface IResultService {
    ResultResponse createResult(ResultRequest resultRequest) throws Exception;
}

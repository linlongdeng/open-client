package com.ktvme.wj.openclient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AogorithmTest {
	private String appId = "680486"; // APP标识,wmc平台分配
	private String md5Key = "EDYWVM3JKGTC"; // MD5签名密匙,wmc平台分配
	private String desKey = "TE4L6BAZ5DCOMGASE3M3ZIKV"; // DES加密密匙,wmc平台分配

	@Test
	public void testSendRequest() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("method", "echo.test");
		String resultJson = ClientHelper.sendRequest(
				"http://127.0.0.1:3007/onlinemarket/aa", md5Key, desKey, appId,
				JsonUtil.toJsonString(params));
		System.out.println(resultJson);
	}

	@Test
	public void test() {
		Map<String, Object> dto = new HashMap<>();
		dto.put("onlineorderid", 212);
		dto.put("oldshoppaymentid", 171);
		dto.put("quitmoney", 0.01);
		dto.put("companyid", 26);
		dto.put("wechatpubinfoid", 2);
		dto.put("refundsake", "买错了");
		String jsonContent = JsonUtil.toJsonString(dto);
		System.out.println(jsonContent);
		String sign = Algorithm.md5Encrypt(jsonContent + "&" + md5Key);
		System.out.println(sign);
		String desContent = Algorithm.TrippleEncrypt(jsonContent, desKey);
		System.out.println(desContent);
	}
}

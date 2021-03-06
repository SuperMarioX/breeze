package com.le.ag.breeze.connector.processor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import com.le.ag.breeze.message.RequestMessageFacade;

/**
 * 
 * @author liyixiang
 * @Info
 * * * @Company leEco
 * * * @Email <liyixiang@le.com>
 * * * @Team SmartConnected
 * @date 2016年4月5日
 * @since JDK 1.7
 * @Function 处理器模版 统一规范处理流程
 */
public abstract class HttpProcessorTemplate implements ProcessorTemplate{

	/**
	 * 
	 * @use 处理规范，维护执行流程顺序
	 * @param
	 * @return
	 */
	public void process(ChannelHandlerContext ctx,FullHttpRequest request) throws Exception{
		Object result;
		//请求封装
		RequestMessageFacade requestMsg = encapsulate(request,ctx);
		//请求拦截
		String interceptResult = interceptRequest(requestMsg);
		
		if(HttpResponseStatus.OK.reasonPhrase().equals(interceptResult)){
			//服务定位
			Object invokeService = serviceLocate(requestMsg);
			//调用
			result = execute(invokeService,requestMsg);
		}else {
			result = interceptResult;
		}
		//结果处理
		sendResponse(ctx,result);
	}
	
	/**
	 * 
	 * @use 请求封装
	 * @param
	 * @return
	 */
	protected abstract RequestMessageFacade encapsulate(FullHttpRequest request,ChannelHandlerContext ctx) throws Exception;
	
	/**
	 * 
	 * @use 请求拦截器
	 * @param
	 * @return
	 */
	protected abstract String interceptRequest(RequestMessageFacade request) throws Exception;
	

	/**
	 * 
	 * @use 服务资源定位
	 * @param
	 * @return
	 */
	protected abstract Object serviceLocate(RequestMessageFacade request) throws Exception;
	
	/**
	 * 
	 * @use 执行
	 * @param
	 * @return
	 */
	protected abstract Object execute(Object invokeService,RequestMessageFacade requestMsgCtx) throws Exception;
	
	/**
	 * 
	 * @use 响应发送
	 * @param
	 * @return
	 */
	protected abstract void sendResponse(ChannelHandlerContext ctx,Object result) throws Exception;
}

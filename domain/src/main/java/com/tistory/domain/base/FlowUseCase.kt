package com.tistory.domain.base

abstract class FlowUseCase<in Params : Any, out Result> {

    protected abstract fun build(params: Params): Result
}
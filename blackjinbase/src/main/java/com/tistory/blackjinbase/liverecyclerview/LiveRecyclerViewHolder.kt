package com.tistory.blackjinbase.liverecyclerview

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

abstract class LiveRecyclerViewHolder<VM : Any>(
    private val binding: ViewDataBinding,
    private val lifecycleOwner: LifecycleOwner?
) : RecyclerView.ViewHolder(binding.root) {
    lateinit var viewModel: VM

    fun bind(viewModel: VM) {
        this.viewModel = viewModel
        binding.setVariable(viewModelId, viewModel)
        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()
    }

    /**
     * if viewModel name is not "model", override this method to set the other view model name
     *
     * @return viewModel Id
     */
    protected abstract val viewModelId: Int

}
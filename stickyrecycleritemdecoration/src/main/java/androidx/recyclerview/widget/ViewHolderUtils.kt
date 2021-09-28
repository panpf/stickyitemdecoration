/*
 * Copyright (C) 2021 panpf <panpfpanpf@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package androidx.recyclerview.widget

import androidx.core.os.TraceCompat

var RecyclerView.ViewHolder.bindingAdapterCompat: RecyclerView.Adapter<*>?
    get() = mBindingAdapter
    set(value) {
        mBindingAdapter = value
    }

fun RecyclerView.ViewHolder.rootBindInit() {
    setFlags(
        RecyclerView.ViewHolder.FLAG_BOUND,
        RecyclerView.ViewHolder.FLAG_BOUND or RecyclerView.ViewHolder.FLAG_UPDATE or RecyclerView.ViewHolder.FLAG_INVALID
                or RecyclerView.ViewHolder.FLAG_ADAPTER_POSITION_UNKNOWN
    )
    TraceCompat.beginSection(RecyclerView.TRACE_BIND_VIEW_TAG)
}

var RecyclerView.ViewHolder.positionCompat: Int
    get() = mPosition
    set(value) {
        mPosition = value
    }

var RecyclerView.ViewHolder.ownerRecyclerViewCompat: RecyclerView
    get() = mOwnerRecyclerView
    set(value) {
        mOwnerRecyclerView = value
    }
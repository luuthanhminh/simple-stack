package com.zhuinden.simplestackdemoexamplemvp.features.tasks

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.zhuinden.simplestackdemoexamplemvp.R
import com.zhuinden.simplestackdemoexamplemvp.domain.Task
import com.zhuinden.simplestackdemoexamplemvp.util.Preconditions.checkNotNull
import com.zhuinden.simplestackdemoexamplemvp.util.inflate
import com.zhuinden.simplestackdemoexamplemvp.util.onClick
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.task_item.*
import java.util.*

class TasksAdapter(
    private var tasks: List<Task>,
    private val itemListener: TaskItemListener
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    var data: List<Task>
        get() = Collections.unmodifiableList(tasks)
        set(tasks) {
            this.tasks = checkNotNull(tasks)
        }

    class TaskViewHolder(
        override val containerView: View,
        private val itemListener: TaskItemListener
    ) : LayoutContainer, RecyclerView.ViewHolder(containerView) {
        lateinit var task: Task

        private val rowClickListener = View.OnClickListener { _ -> itemListener.onTaskRowClicked(task) }

        private val context = containerView.context

        init {
            containerView.setOnClickListener(rowClickListener)
            complete.onClick {
                itemListener.onTaskCheckClicked(task)
            }
        }

        fun bind(task: Task) {
            this.task = task
            title.text = task.titleForList
            complete.isChecked = task.isCompleted
            containerView.setBackgroundResource(when {
                task.isCompleted -> R.drawable.list_completed_touch_feedback
                else -> R.drawable.touch_feedback
            })
        }
    }

    interface TaskItemListener {
        fun onTaskRowClicked(task: Task)

        fun onTaskCheckClicked(task: Task)
    }

    init {
        data = tasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(parent.inflate(R.layout.task_item), itemListener)

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size
}


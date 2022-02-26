package com.androidcourse.leaguewiki.items

import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.androidcourse.leaguewiki.R
import com.androidcourse.leaguewiki.databinding.VideoItemBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class VideoItem : AbstractBindingItem<VideoItemBinding>() {

    var urlVideo: String? = null

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): VideoItemBinding {
        return VideoItemBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: VideoItemBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.videoView.setVideoURI(Uri.parse(urlVideo))
        binding.videoView.start()

        binding.videoView.setOnInfoListener { _, what, _ ->
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                binding.loadingLayout.root.isVisible = false
            }
            return@setOnInfoListener true
        }


        binding.videoView.setOnCompletionListener {
            binding.videoView.start()
        }
    }

    override val type: Int = R.id.video_item
}

fun videoItem(block: VideoItem.() -> Unit) = VideoItem().apply(block)
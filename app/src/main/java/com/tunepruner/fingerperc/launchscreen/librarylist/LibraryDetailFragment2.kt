//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.fragment.app.FragmentTransaction
//import com.google.android.youtube.player.YouTubeInitializationResult
//import com.google.android.youtube.player.YouTubePlayer
//import com.google.android.youtube.player.YouTubePlayerFragment
//import com.google.android.youtube.player.YouTubePlayerSupportFragment
//import com.tunepruner.fingerperc.R
////import android.support.v4.app.Fragment
//import android.support.v4.*
//
//class YoutubeFragment : Fragment() {
//    private var myContext: FragmentActivity? = null
//    private var youTubePlayer: YouTubePlayer? = null
//
////    override fun onAttach(activity: Activity?) {
////        super.onAttach(activity)
////        if (activity is FragmentActivity) {
////            myContext = activity as FragmentActivity?
////        }
////    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val rootView: View = inflater.inflate(R.layout.activity_you_tube_api, container, false)
//        val youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance() as YouTubePlayerSupportFragment
//        val transaction: FragmentTransaction = getChildFragmentManager().beginTransaction()
//        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit()
//        youTubePlayerFragment.initialize(
//            "DEVELOPER_KEY",
//            object : YouTubePlayer.OnInitializedListener {
//                override fun onInitializationSuccess(
//                    arg0: YouTubePlayer.Provider,
//                    youTubePlayer: YouTubePlayer,
//                    b: Boolean
//                ) {
//                    if (!b) {
//                        this@YoutubeFragment.youTubePlayer = youTubePlayer
//                        this@YoutubeFragment.youTubePlayer!!.loadVideo("2zNSgSzhBfM")
//                        this@YoutubeFragment.youTubePlayer!!.play()
//                    }
//                }
//
//                override fun onInitializationFailure(
//                    arg0: YouTubePlayer.Provider,
//                    arg1: YouTubeInitializationResult
//                ) {
//                    // TODO Auto-generated method stub
//                }
//            })
//    }
//
//    companion object {
//        private const val YoutubeDeveloperKey = "xyz"
//        private const val RECOVERY_DIALOG_REQUEST = 1
//    }
//}
package com.example.grubgamble

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue.arrayUnion
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*


class ProfileFragment : Fragment() {

    //private lateinit var  imageUri: Uri
    var databaseReference : DatabaseReference? = null
    var database: FirebaseDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        //get xml id's
        val userName = view.findViewById<TextView>(R.id.user_name)
        val userPts = view.findViewById<TextView>(R.id.user_points)
        val userPic = view.findViewById<ImageView>(R.id.profile_pic)
        val listView = view.findViewById<ListView>(R.id.friendsList)
        val uploadPic = view.findViewById<Button>(R.id.uploadPic)
        val usernameText = view.findViewById<EditText>(R.id.usernameText)
        val buttonAddFriend = view.findViewById<Button>(R.id.buttonAddFriend)
        val builder = AlertDialog.Builder(view.context)

        //pull the users profile
        pullProfile(userName, userPts, userPic)

        //set click listener for the users photo that opens up the gallery and lets them choose a photo
        userPic.setOnClickListener {
            Log.d("ProfileFragment", "Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        //set click listener that lets the user assign their photo to their firebase acc
        uploadPic.setOnClickListener {
            uploadImageToFirebaseStorage()
        }

        var listItems:ArrayList<String> = ArrayList()

        //create a list of all users to search for when adding to friends list
        val userdocRef = Firebase.firestore.collection("Users")
        val allUsers: ArrayList<String> = ArrayList()

        userdocRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(!allUsers.contains(document.data["username"].toString())) {
                        allUsers.add(document.data["username"].toString())
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("tag", "Error getting documents: ", exception)
            }

        val adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter

        //get friends list from google firebase
        val userid = FirebaseAuth.getInstance().currentUser.uid
        val mydocRef = Firebase.firestore.collection("Users").document(userid)

        mydocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //copy array contents
                    val friends = document.data?.get("friends") as ArrayList<*>
                    for(i in 0 until friends.size) {
                        listItems.add(friends[i] as String)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Log.d("tag", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("tag", "get failed with ", exception)
            }

        //searching and adding friends
        buttonAddFriend.setOnClickListener {
            val nameAdded = usernameText.text.toString()
            if(nameAdded.isEmpty()){
                builder.setTitle("Input is Empty")
                builder.setMessage("Please enter a valid username..")
                builder.setPositiveButton(
                    "OK") { dialog, id ->
                }.show()
            } else if(!allUsers.contains(nameAdded)) {
                builder.setTitle("Cannot find user")
                builder.setMessage("User does not exist please try again..")
                builder.setPositiveButton(
                    "OK") { dialog, id ->
                }.show()
            } else {
                if(!listItems.contains(nameAdded) /*&& nameAdded != myuserName*/) {
                    mydocRef.update("friends", arrayUnion(nameAdded))
                    listItems.add(nameAdded)
                    adapter.notifyDataSetChanged() //updates listview adapter
                    usernameText.text.clear()
                    usernameText.clearFocus()
                }

            }
        }

        return view
    }

    //opens up the phones gallery and assigns the selected image to the imageview
    var uri: Uri? = null
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("ProfileFragment", "Photo was selected")
            uri = data.data
            view?.findViewById<ImageView>(R.id.profile_pic)?.setImageURI(uri)

        }
    }

    //uploads selected image to firebase when called by the upload button
    private fun uploadImageToFirebaseStorage(){
        if (uri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(uri!!).addOnSuccessListener {
            Log.d("ProfileFragment", "Successfully uploaded image: ${it.metadata?.path}")

            ref.downloadUrl.addOnSuccessListener {
                Log.d("ProfileFragment", "File Location: $it")
                saveProfilePicToUser(it.toString())

            }
        }
    }
    //assigns the selected image to the firebase user
    private fun saveProfilePicToUser(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().currentUser.uid
        val docRef = Firebase.firestore.collection("Users").document(uid)
        docRef.update("profilePic", profileImageUrl)

    }

    //pulls the users profile information and upadates the username, points, and profile pic fields
    private fun pullProfile(userName: TextView, userPts: TextView, userPic: ImageView){

        val db = Firebase.firestore
        val uid = FirebaseAuth.getInstance().currentUser.uid
        db.collection("Users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userName.text= document.data?.get("username").toString()
                    userPts.text = document.data?.get("points").toString() + " points"
                    Picasso.get().load(document.data?.get("profilePic").toString()).into(userPic)
                    Log.d("tag", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("tag", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("tag", "get failed with ", exception)
            }
    }
}
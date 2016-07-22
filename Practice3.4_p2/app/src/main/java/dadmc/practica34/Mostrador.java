package dadmc.practica34;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Mostrador.MostradorInteractionListener} interface
 * to handle interaction events.
 */
public class Mostrador extends Fragment {

    private MostradorInteractionListener mListener;

    TextView txtPoem;

    private static final String DEBUGTAG = "Practica3_4_Mostrador";

    public Mostrador() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        Log.i(DEBUGTAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_mostrador, container, false);
        TextView tv = (TextView) v.findViewById(R.id.txtPoem);
        if(savedInstanceState != null){
            Log.i(DEBUGTAG, "setText onCreateView");
            tv.setText(savedInstanceState.getString("text").toString());
        }else{

        }
        // Inflate the layout for this fragment

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        TextView tv = (TextView) getView().findViewById(R.id.txtPoem);
        state.putString("text", tv.getText().toString());
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/


    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);
        txtPoem = (TextView) getView().findViewById(R.id.txtPoem);
        if(state != null){
            mListener.setPoem(state.getString("text"));
        }else{
            mListener.setPoem("");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MostradorInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface MostradorInteractionListener {
        // TODO: Update argument type and name
        public void setPoem(String poem);
    }

}

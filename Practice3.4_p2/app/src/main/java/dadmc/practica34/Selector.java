package dadmc.practica34;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Selector.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Selector extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button btnBecker;
    private Button btnMachado;
    private Button btnQuevedo;

    public Selector() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selector, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);
        btnBecker = (Button) getView().findViewById(R.id.btnBecker);
        btnBecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeSelector(0);
            }
        });

        btnMachado = (Button) getView().findViewById(R.id.btnMachado);
        btnMachado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mListener.changeSelector(1);
            }
        });

        btnQuevedo = (Button) getView().findViewById(R.id.btnQuevedo);
        btnQuevedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mListener.changeSelector(2);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void changeSelector(int option);
    }

}

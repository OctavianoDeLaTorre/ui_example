package adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorias.vista.R;

import java.util.ArrayList;

import modelo.Tutor;

public class AdaptadorTutor extends RecyclerView.Adapter<TutorViewHolder>{
    private ArrayList<Tutor> tutores;

    public AdaptadorTutor(ArrayList<Tutor> tutores) {
        this.tutores = tutores;
    }

    @NonNull
    @Override
    public TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutor, parent, false);
        TutorViewHolder pvh = new TutorViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewHolder holder, int position) {
        holder.tutor.setText(tutores.get(position).getNombreTutor()+" "+tutores.get(position).getApellidoTutor().replace("+"," "));
        holder.alumno.setText(tutores.get(position).getNombreAlumno()+" "+tutores.get(position).getApellidoAlumno().replace("+"," "));
        holder.personPhoto.setImageResource(R.drawable.usuario);
    }

    @Override
    public int getItemCount() {
        return tutores.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

class TutorViewHolder extends RecyclerView.ViewHolder {
    CardView cv;
    TextView tutor;
    TextView alumno;
    ImageView personPhoto;

    TutorViewHolder(View itemView) {
        super(itemView);
        cv = (CardView)itemView.findViewById(R.id.cvTutor);
        tutor = (TextView)itemView.findViewById(R.id.nombreTutor);
        alumno = (TextView)itemView.findViewById(R.id.carreraTutor);
        personPhoto = (ImageView)itemView.findViewById(R.id.person_photo_tutor);
    }
}


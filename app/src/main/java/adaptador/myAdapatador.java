package adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ui.R;

import java.util.ArrayList;

import modelo.Alumno;

public class myAdapatador extends RecyclerView.Adapter<PersonViewHolder>{

    private ArrayList<Alumno> alumnos;

    public myAdapatador(ArrayList<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linear_laout, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        holder.nombre.setText(alumnos.get(position).getNombre()+" "+alumnos.get(position).getApellido().replace("+"," "));
        holder.carrera.setText("Carrera: "+alumnos.get(position).getCarrera()+", Semestre"+alumnos.get(position).getSemestre());
        holder.personPhoto.setImageResource(R.drawable.agregar);
    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

class PersonViewHolder extends RecyclerView.ViewHolder {
    CardView cv;
    TextView nombre;
    TextView carrera;
    ImageView personPhoto;

    PersonViewHolder(View itemView) {
        super(itemView);
        cv = (CardView)itemView.findViewById(R.id.cvBajas);
        nombre = (TextView)itemView.findViewById(R.id.nombreAlumno);
        carrera = (TextView)itemView.findViewById(R.id.carreraAlumno);
        personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
    }
}

package volservice.iia.apivoyage.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import volservice.iia.apivoyage.R;

public class ContactFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.contactImgGit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.txt_contact_gitlink))));
            }
        });

        view.findViewById(R.id.contactSendEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

    }

    protected void sendEmail() {
        String TO = getString(R.string.txt_contact_mail);
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", TO, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.txt_contact_sujet));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.txt_contact_content));
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));

//        emailIntent.setDataAndType(Uri.parse("mailto:"), "text/plain");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.txt_contact_sujet));
//        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.txt_contact_content));

        try {
            startActivity(Intent.createChooser(intent, getString(R.string.txt_contact_sending)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this.getContext(), getString(R.string.txt_contact_sending), Toast.LENGTH_SHORT).show();
        }
    }
}

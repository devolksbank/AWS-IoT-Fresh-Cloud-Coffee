package devolksbank.nl.statecloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class DisplayAboutPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_about_page);

        openAboutPage();

    }

    private void openAboutPage() {
        View aboutPage = new AboutPage(this)
                .setImage(R.drawable.about_icon)
                .setDescription(getString(R.string.about_description))
                .addWebsite(getString(R.string.blog_url))
                .addEmail(getString(R.string.email))
                .addItem(getLicenseElement())
                .addItem(getCreditsElement())
                .create();

        setContentView(aboutPage);
    }


    public Element getCreditsElement() {
        Element creditsElement = new Element();
        creditsElement.setTitle(getString(R.string.credits));
        creditsElement.setIconDrawable(R.drawable.ic_cc);
        return creditsElement;
    }

    public Element getLicenseElement() {
        Element licenseElement = new Element();
        licenseElement.setTitle(getString(R.string.license));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.license_url)));
        licenseElement.setIconDrawable(R.drawable.ic_copyright_black_24dp);
        licenseElement.setIntent(browserIntent);
        return licenseElement;
    }

}

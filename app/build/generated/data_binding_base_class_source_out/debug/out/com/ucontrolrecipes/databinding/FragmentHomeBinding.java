// Generated by view binder compiler. Do not edit!
package com.ucontrolrecipes.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.ucontrolrecipes.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentHomeBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView imageViewRandomRecipe1;

  @NonNull
  public final ImageView imageViewRandomRecipe2;

  @NonNull
  public final ImageView imageViewRandomRecipe3;

  @NonNull
  public final ImageView imageViewRandomRecipe4;

  @NonNull
  public final ImageView imageViewRandomRecipe5;

  @NonNull
  public final ImageView imageViewRandomRecipe6;

  @NonNull
  public final ImageView imageViewRandomRecipe7;

  @NonNull
  public final ImageView imageViewRandomRecipe8;

  @NonNull
  public final View view;

  @NonNull
  public final View view2;

  @NonNull
  public final View view3;

  private FragmentHomeBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView imageViewRandomRecipe1, @NonNull ImageView imageViewRandomRecipe2,
      @NonNull ImageView imageViewRandomRecipe3, @NonNull ImageView imageViewRandomRecipe4,
      @NonNull ImageView imageViewRandomRecipe5, @NonNull ImageView imageViewRandomRecipe6,
      @NonNull ImageView imageViewRandomRecipe7, @NonNull ImageView imageViewRandomRecipe8,
      @NonNull View view, @NonNull View view2, @NonNull View view3) {
    this.rootView = rootView;
    this.imageViewRandomRecipe1 = imageViewRandomRecipe1;
    this.imageViewRandomRecipe2 = imageViewRandomRecipe2;
    this.imageViewRandomRecipe3 = imageViewRandomRecipe3;
    this.imageViewRandomRecipe4 = imageViewRandomRecipe4;
    this.imageViewRandomRecipe5 = imageViewRandomRecipe5;
    this.imageViewRandomRecipe6 = imageViewRandomRecipe6;
    this.imageViewRandomRecipe7 = imageViewRandomRecipe7;
    this.imageViewRandomRecipe8 = imageViewRandomRecipe8;
    this.view = view;
    this.view2 = view2;
    this.view3 = view3;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imageViewRandomRecipe1;
      ImageView imageViewRandomRecipe1 = ViewBindings.findChildViewById(rootView, id);
      if (imageViewRandomRecipe1 == null) {
        break missingId;
      }

      id = R.id.imageViewRandomRecipe2;
      ImageView imageViewRandomRecipe2 = ViewBindings.findChildViewById(rootView, id);
      if (imageViewRandomRecipe2 == null) {
        break missingId;
      }

      id = R.id.imageViewRandomRecipe3;
      ImageView imageViewRandomRecipe3 = ViewBindings.findChildViewById(rootView, id);
      if (imageViewRandomRecipe3 == null) {
        break missingId;
      }

      id = R.id.imageViewRandomRecipe4;
      ImageView imageViewRandomRecipe4 = ViewBindings.findChildViewById(rootView, id);
      if (imageViewRandomRecipe4 == null) {
        break missingId;
      }

      id = R.id.imageViewRandomRecipe5;
      ImageView imageViewRandomRecipe5 = ViewBindings.findChildViewById(rootView, id);
      if (imageViewRandomRecipe5 == null) {
        break missingId;
      }

      id = R.id.imageViewRandomRecipe6;
      ImageView imageViewRandomRecipe6 = ViewBindings.findChildViewById(rootView, id);
      if (imageViewRandomRecipe6 == null) {
        break missingId;
      }

      id = R.id.imageViewRandomRecipe7;
      ImageView imageViewRandomRecipe7 = ViewBindings.findChildViewById(rootView, id);
      if (imageViewRandomRecipe7 == null) {
        break missingId;
      }

      id = R.id.imageViewRandomRecipe8;
      ImageView imageViewRandomRecipe8 = ViewBindings.findChildViewById(rootView, id);
      if (imageViewRandomRecipe8 == null) {
        break missingId;
      }

      id = R.id.view;
      View view = ViewBindings.findChildViewById(rootView, id);
      if (view == null) {
        break missingId;
      }

      id = R.id.view2;
      View view2 = ViewBindings.findChildViewById(rootView, id);
      if (view2 == null) {
        break missingId;
      }

      id = R.id.view3;
      View view3 = ViewBindings.findChildViewById(rootView, id);
      if (view3 == null) {
        break missingId;
      }

      return new FragmentHomeBinding((ConstraintLayout) rootView, imageViewRandomRecipe1,
          imageViewRandomRecipe2, imageViewRandomRecipe3, imageViewRandomRecipe4,
          imageViewRandomRecipe5, imageViewRandomRecipe6, imageViewRandomRecipe7,
          imageViewRandomRecipe8, view, view2, view3);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

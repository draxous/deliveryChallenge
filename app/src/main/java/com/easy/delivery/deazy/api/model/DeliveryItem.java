package com.easy.delivery.deazy.api.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressLint("ParcelCreator")
public class DeliveryItem implements Parcelable {
	public DeliveryItem(Parcel in) {
		this.imageUrl = in.readString();
		this.description = in.readString();
		this.location = in.readParcelable(Location.class.getClassLoader());
		this.id = in.readInt();
	}

	@SerializedName("imageUrl")
	private String imageUrl;

	@SerializedName("description")
	private String description;

	@SerializedName("location")
	private Location location;

	@SerializedName("id")
	private int id;

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setLocation(Location location){
		this.location = location;
	}

	public Location getLocation(){
		return location;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"DeliveryItem{" +
			"imageUrl = '" + imageUrl + '\'' + 
			",description = '" + description + '\'' + 
			",location = '" + location + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public DeliveryItem createFromParcel(Parcel in ) {
			return new DeliveryItem( in );
		}

		public DeliveryItem[] newArray(int size) {
			return new DeliveryItem[size];
		}
	};

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(imageUrl);
		dest.writeString(description);
		dest.writeParcelable(location,flags);
	}


}
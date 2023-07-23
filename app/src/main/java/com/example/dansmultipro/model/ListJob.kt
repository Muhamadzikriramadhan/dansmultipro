package com.example.dansmultipro.model

import com.google.gson.annotations.SerializedName

class ListJob(@SerializedName("id"           ) var id          : String? = null,
              @SerializedName("type"         ) var type        : String? = null,
              @SerializedName("url"          ) var url         : String? = null,
              @SerializedName("created_at"   ) var createdAt   : String? = null,
              @SerializedName("company"      ) var company     : String? = null,
              @SerializedName("company_url"  ) var companyUrl  : String? = null,
              @SerializedName("location"     ) var location    : String? = null,
              @SerializedName("title"        ) var title       : String? = null,
              @SerializedName("description"  ) var description : String? = null,
              @SerializedName("how_to_apply" ) var howToApply  : String? = null,
              @SerializedName("company_logo" ) var companyLogo : String? = null) {
}
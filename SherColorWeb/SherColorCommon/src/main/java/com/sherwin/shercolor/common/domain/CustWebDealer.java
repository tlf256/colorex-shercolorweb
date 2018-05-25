package com.sherwin.shercolor.common.domain;

	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
	import javax.validation.constraints.NotNull;

	@Entity
	@Table(name="CUSTWEBDEALER")
	@IdClass(CustWebDealerPK.class)
	public class CustWebDealer {

		private String 	customerId;
		private String 	dealerName;
		private String  dlrPhoneNbr;
		private int		homeStore;
		private String  comments;

		@Id
		@Column(name="customerId")
		@NotNull
		public String getCustomerId() {
			return customerId;
		}
		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}

		@Column(name="dealerName")
		public String getDealerName() {
			return dealerName;
		}
		public void setDealerName(String dealerName) {
			this.dealerName = dealerName;
		}

		@Column(name="dlrPhoneNbr")
		public String getDlrPhoneNbr() {
			return dlrPhoneNbr;
		}
		public void setDlrPhoneNbr(String dlrPhoneNbr) {
			this.dlrPhoneNbr = dlrPhoneNbr;
		}

		@Column(name="dlrHomeStore")
		public int getHomeStore() {
			return homeStore;
		}
		public void setHomeStore(int homeStore) {
			this.homeStore = homeStore;
		}
		
		@Column(name="comments")
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
		
}

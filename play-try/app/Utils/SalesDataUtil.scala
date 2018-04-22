package Utils


case class SalesInputData(userId: Int, productId: String, gender: String, age: String, occupation: String, city: String, cityTenure: String, maritalStatus: String, productCategory1: String, productCategory2: String, productCategory3: String, purchaseAmount: String)

case class AgeGroupPurchasesSalesInputData( age: String,  purchaseAmount: String)

case class ProductCategoryCitySalesInputData( productCategory1: String,city: String)

case class UserAgePurchaseSalesInputData(gender: String, age: String, purchaseAmount: String)


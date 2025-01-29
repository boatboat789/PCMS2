package th.co.wacoal.atech.pcms2.entities;

public class DataImport {
   private String[] fields;

@SuppressWarnings("null")
public DataImport(int max, String[] fields) throws IndexOutOfBoundsException {
      if (fields == null && fields.length != max) {
         throw new IndexOutOfBoundsException();
      } else {
         this.fields = fields;
      }
   }

   public int getMax() {
      return this.fields.length;
   }

   public String getField(int index) {
      return index < this.fields.length ? this.fields[index] : "";
   }

   public String[] getFields() {
      return this.fields;
   }

   public void setField(int index, String value) {
      if (index < this.fields.length) {
         this.fields[index] = value;
      }

   }
}

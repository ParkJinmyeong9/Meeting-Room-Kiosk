package meeting_room;

import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;

public class SeedEncoding {

	private static byte pbUserKey[] = "0123456789abcdef".getBytes(); // 16
	private static byte pbCipher[] = new byte[50];
	
	//seed 암호화
	public static byte[] encrypt(String str){
		byte[] userBytes = str.getBytes();
		byte pbData[] = new byte[16];
			
		for(int i=0; i<userBytes.length; i++) {
			if (i < userBytes.length) 
				pbData[i] = userBytes[i];
			else
				pbData[i] = 0x00;
		}
		
		//암호화 함수 호출
		pbCipher = KISA_SEED_ECB.SEED_ECB_Encrypt(pbUserKey, pbData,  0, pbData.length);
		
		Encoder encoder = Base64.getEncoder(); 
		byte[] encArray = encoder.encode(Arrays.copyOf(pbCipher, 16)); // 인코딩할 바이트 배열의 길이를 16으로 수정
		 
		return encArray;
	}
}
function [TranformedIm] = EdgeDetection(Im)
TranformedIm = zeros(size(Im, 1)-2, size(Im, 2)-2);

Dx = 0;
Dy = 0;
for i = 2:1:size(Im, 1)-1
    for j = 2:1:size(Im, 2)-1
        Dx = 2*Im(i-1, j-1) - 2*Im(i-1, j+1) + Im(i, j - 1) - Im(i, j+1) + 2*Im(i + 1, j - 1) - 2*Im(i + 1, j +1);
        Dy = 2*Im(i-1, j-1) - 2*Im(i+1, j-1) + Im(i - 1, j) - Im(i+1, j) + 2*Im(i - 1, j + 1) - 2*Im(i + 1, j + 1);
        sqrt(double(Dx) ^ 2 + double(Dy) ^ 2);
        TranformedIm(i - 1, j - 1) = sqrt(double(Dx) ^ 2 + double(Dy) ^ 2);
    end
end
max_element = max(TranformedIm(:));
TranformedIm = TranformedIm./max_element;
imshow(TranformedIm);

end


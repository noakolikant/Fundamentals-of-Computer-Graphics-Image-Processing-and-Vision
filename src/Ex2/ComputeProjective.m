function [TranformedIm] = ComputeProjective(Im, H)

%ComputeProjective This function computes the projective H on the image Im
    tform = maketform('projective', H);
    udata = [0 1];  vdata = [0 1];
    TranformedIm = imtransform(Im, tform, 'bicubic', 'udata', udata, ...
    'vdata', vdata, 'size', size(Im), 'fill', 128);


end

